/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fi.softala.ttl.dao.PassiDAO;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.model.User;
import fi.softala.ttl.service.PassiService;
import fi.softala.ttl.service.SecurityService;
import fi.softala.ttl.service.UserService;
import fi.softala.ttl.validator.UserValidator;

@EnableWebMvc
@Controller
@Scope("session")
@SessionAttributes({ "categories", "defaultGroup", "user", "groups", "groupMembers", "instructorsDetails", "isAnsweredMap", "message", "memberDetails", "newGroup", "newMember",
		"selectedCategory", "selectedGroup", "selectedMember", "selectedWorksheet", "worksheets", "worksheetContent", "worksheetAnswers" })
public class PassiController {

	final static Logger logger = LoggerFactory.getLogger(PassiController.class);

	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
	
	@Autowired
	ServletContext context;
	
	@Autowired
	PassiService passiService;

	@Inject
	private PassiDAO dao;

	public PassiDAO getDao() {
		return dao;
	}

	public void setDao(PassiDAO dao) {
		this.dao = dao;
	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Tarkista tunnuksesi");
		} else {
			model.addObject("error", "");
		}
		if (logout != null) {
			model.addObject("message", "Olet kirjautunut ulos");
		} else {
			model.addObject("message", "");
		}
		model.setViewName("login");
		/* if (logger.isDebugEnabled()) {logger.debug("loginPage is executed!");}
		   logger.error("This is Error message", new Exception("Testing")); */
		return model;
	}

	// Initiate session variables right after login
	@RequestMapping(value = { "/init" }, method = RequestMethod.GET)
	public String init(final RedirectAttributes redirectAttributes) {
		
		// Authenticated user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		redirectAttributes.addFlashAttribute("user", auth.getName());
		
		// Session attributes for dropdown selection
		redirectAttributes.addFlashAttribute("categories", passiService.getCategoriesDTO());
		redirectAttributes.addFlashAttribute("groups", passiService.getGroupsDTO());
		redirectAttributes.addFlashAttribute("worksheets", new ArrayList<WorksheetDTO>());
		
		// Session attributes for selections
		redirectAttributes.addFlashAttribute("selectedCategory", 0);
		redirectAttributes.addFlashAttribute("selectedGroup", 0);
		redirectAttributes.addFlashAttribute("selectedMember", 0);
		redirectAttributes.addFlashAttribute("selectedWorksheet", 0);
		
		redirectAttributes.addFlashAttribute("groupMembers", new ArrayList<User>());
		redirectAttributes.addFlashAttribute("newGroup", new Group());
		redirectAttributes.addFlashAttribute("newMember", new User());
		
		return "redirect:/index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String indexPage(Model model) {
		return "index";
	}

	@RequestMapping(value = "/index/{page}", method = RequestMethod.GET)
	public String pageNavigation(Model model,
			@PathVariable(value = "page") String page,
			@ModelAttribute(value = "message") String message) {
		model.addAttribute("groups", passiService.getAllGroups());
		model.addAttribute("message", message);
		return page;
	}

	@RequestMapping(value = "/expired", method = RequestMethod.GET)
	public ModelAndView expiredPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("expired");
		return model;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }
	
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(userForm);
        securityService.autologin(userForm.getUsername(), userForm.getConfirmPassword());
        return "redirect:/index";
    }

	// 1. SELECT GROUP
	@RequestMapping(value = "/selectGroup", method = RequestMethod.POST)
	public String selectGroup(Model model, @RequestParam int groupID) {
		model.addAttribute("groupMembers", passiService.getGroupMembers(groupID));
		model.addAttribute("instructorsDetails", passiService.getInstructorsDetails(groupID));
		model.addAttribute("selectedCategory", 0);
		model.addAttribute("selectedGroup", groupID);
		model.addAttribute("selectedMember", 0);
		model.addAttribute("selectedWorksheet", 0);
		logger.info("selectGroup completed");
		return "index";
	}
	
	// 2. SELECT CATEGORY
	@RequestMapping(value = "/selectCategory", method = RequestMethod.POST)
	public String selectCategory(Model model, @RequestParam int categoryID,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("worksheets") ArrayList<WorksheetDTO> worksheets) {
		model.addAttribute("selectedCategory", categoryID);
		model.addAttribute("selectedMember", 0);
		model.addAttribute("selectedWorksheet", 0);
		model.addAttribute("worksheets", passiService.getWorksheetsDTO(groupID, categoryID));
		logger.info("selectCategory completed");
		return "index";
	}
	
	// 3. SELECT WORKSHEET
	@RequestMapping(value = "/selectWorksheet", method = RequestMethod.POST)
	public String selectWorksheet(Model model, @RequestParam int worksheetID,
			@ModelAttribute("groupMembers") ArrayList<User> groupMembers,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("selectedCategory") int categoryID) {
		model.addAttribute("groupMembers", passiService.getGroupMembers(groupID));
		model.addAttribute("isAnsweredMap", passiService.getIsAnsweredMap(worksheetID, groupMembers));
		model.addAttribute("selectedMember", 0);
		model.addAttribute("selectedWorksheet", worksheetID);
		logger.info("selectWorksheet completed");
		return "index";
	}

	// 4. SELECT MEMBER (GET WORKSHEET WITH POSSIBLE ANSWERS)
	@RequestMapping(value = "/selectMember", method = RequestMethod.POST)
	public String selectMember(Model model, @RequestParam int userID,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("selectedWorksheet") int worksheetID) {
		model.addAttribute("memberDetails", passiService.getMemberDetails(userID));
		model.addAttribute("selectedMember", userID);
		model.addAttribute("worksheetAnswers", passiService.getWorksheetAnswers(worksheetID, userID));
		model.addAttribute("worksheetContent", passiService.getWorksheetContent(worksheetID));
		logger.info("selectMember completed");
		return "index";
	}

	@RequestMapping(value = "/saveWaypointFeedback", method = RequestMethod.POST)
	public String saveWaypointFeedback(Model model,
			@RequestParam int answerWaypointID,
			@RequestParam String instructorComment,
			@RequestParam int instructorRating,
			@ModelAttribute("selectedWorksheet") int worksheetID,
			@ModelAttribute("selectedMember") int selectedMember) {
		passiService.saveFeadback(answerWaypointID, instructorRating, instructorComment);
		model.addAttribute("worksheetAnswers", passiService.getWorksheetAnswers(worksheetID, selectedMember));
		return "index";
	}

	@RequestMapping(value = "/download/{name}/{type}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable("name") String name,
			@PathVariable("type") String type) throws IOException {
		String rootPath = System.getProperty("catalina.home");
		File file = new File(rootPath + File.separator + "images" + File.separator + name + ".jpg");
		if (!file.exists()) {
			String errorMessage = "Tiedostoa ei löydy";
			// System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			// System.out.println("MIME tunnistamaton");
			mimeType = "application/octet-stream";
		}
		// System.out.println("mimetype : " + mimeType);
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public ModelAndView addGroup(@ModelAttribute("newGroup") Group newGroup) {
		ModelAndView model = new ModelAndView();
		if (dao.addGroup(newGroup)) {
			model.addObject("message", "Ryhmän lisääminen onnistui.");
		} else {
			model.addObject("message", "Ryhmän lisääminen EI onnistunut.");
		}
		//model.addObject("groups", dao.getAllGroups());
		model.setViewName("group");
		return model;
	}

	@RequestMapping(value = "/delGroup", method = RequestMethod.POST)
	public ModelAndView delGroup(@RequestParam int groupID, @ModelAttribute("groups") ArrayList<Group> groups,
			final RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView();
		if (dao.delGroup(groupID)) {
			redirectAttributes.addFlashAttribute("message", "Ryhmän poistaminen onnistui.");
		} else {
			redirectAttributes.addFlashAttribute("message", "Ryhmän poistaminen EI onnistunut.");
		}
		model.setViewName("redirect:/index/group");
		return model;
	}
}