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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fi.softala.ttl.dao.PassiDAO;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Role;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.model.User;
import fi.softala.ttl.service.PassiService;
import fi.softala.ttl.service.UserService;
import fi.softala.ttl.validator.UserValidator;

@EnableWebMvc
@Controller
@Scope("session")
@SessionAttributes({ "categories", "defaultGroup", "user", "userDetails", "groups", "groupMembers", "instructorsDetails", "isAnsweredMap", "message", "memberDetails", "newGroup", "editedGroup", "newMember",
		"selectedCategory", "selectedGroup", "selectedMember", "selectedWorksheet", "worksheets", "worksheetContent", "worksheetAnswers" })
public class PassiController {

	final static Logger logger = LoggerFactory.getLogger(PassiController.class);

	@Autowired
    private UserService userService;
	
	/*
    @Autowired
    private SecurityService securityService;
	*/
	
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
		String username = getAuthUsername();
		redirectAttributes.addFlashAttribute("user", username);
		
		// Get user data
		User userDetails = userService.findByUsername(username);
		redirectAttributes.addFlashAttribute("userDetails", userDetails);
		
		// Session attributes for dropdown selection
		redirectAttributes.addFlashAttribute("categories", passiService.getCategoriesDTO());
		redirectAttributes.addFlashAttribute("groups", passiService.getGroupsDTO(username));
		redirectAttributes.addFlashAttribute("worksheets", new ArrayList<WorksheetDTO>());
		
		// Session attributes for selections
		redirectAttributes.addFlashAttribute("selectedCategory", 0);
		redirectAttributes.addFlashAttribute("selectedGroup", 0);
		redirectAttributes.addFlashAttribute("selectedMember", 0);
		redirectAttributes.addFlashAttribute("selectedWorksheet", 0);
		
		redirectAttributes.addFlashAttribute("groupMembers", new ArrayList<User>());
		redirectAttributes.addFlashAttribute("newGroup", new Group());
		redirectAttributes.addFlashAttribute("newMember", new User());
		redirectAttributes.addFlashAttribute("editedGroup", new Group());
		
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
		model.addAttribute("groups", passiService.getAllGroups(getAuthUsername()));
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
        	model.addAttribute("userForm", userForm);
            return "registration";
        }
        Role role = new Role(2, "ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        userForm.setRoles(roles);
        passiService.saveUser(userForm);
        model.addAttribute("message", "Rekisteröinti onnistui");
        return "login";
    }

	// 1. SELECT GROUP
	@RequestMapping(value = "/selectGroup", method = RequestMethod.POST)
	public String selectGroup(@RequestParam int groupID, final RedirectAttributes ra) {
		ra.addFlashAttribute("groupMembers", passiService.getGroupMembers(groupID));
		ra.addFlashAttribute("instructorsDetails", passiService.getInstructorsDetails(groupID));
		ra.addFlashAttribute("selectedCategory", 0);
		ra.addFlashAttribute("selectedGroup", groupID);
		ra.addFlashAttribute("selectedMember", 0);
		ra.addFlashAttribute("selectedWorksheet", 0);
		logger.info("selectGroup completed");
		return "redirect:/index";
	}
	
	// 2. SELECT CATEGORY
	@RequestMapping(value = "/selectCategory", method = RequestMethod.POST)
	public String selectCategory(@RequestParam int categoryID,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("worksheets") ArrayList<WorksheetDTO> worksheets,
			final RedirectAttributes ra) {
		ra.addFlashAttribute("selectedCategory", categoryID);
		ra.addFlashAttribute("selectedMember", 0);
		ra.addFlashAttribute("selectedWorksheet", 0);
		ra.addFlashAttribute("worksheets", passiService.getWorksheetsDTO(groupID, categoryID));
		logger.info("selectCategory completed");
		return "redirect:/index";
	}
	
	// 3. SELECT WORKSHEET
	@RequestMapping(value = "/selectWorksheet", method = RequestMethod.POST)
	public String selectWorksheet(@RequestParam int worksheetID,
			@ModelAttribute("groupMembers") ArrayList<User> groupMembers,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("selectedCategory") int categoryID,
			final RedirectAttributes ra) {
		ra.addFlashAttribute("groupMembers", passiService.getGroupMembers(groupID));
		ra.addFlashAttribute("isAnsweredMap", passiService.getIsAnsweredMap(worksheetID, groupMembers));
		ra.addFlashAttribute("selectedMember", 0);
		ra.addFlashAttribute("selectedWorksheet", worksheetID);
		logger.info("selectWorksheet completed");
		return "redirect:/index";
	}

	// 4. SELECT MEMBER (GET WORKSHEET WITH POSSIBLE ANSWERS)
	@RequestMapping(value = "/selectMember", method = RequestMethod.POST)
	public String selectMember(@RequestParam int userID,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("selectedWorksheet") int worksheetID,
			final RedirectAttributes ra) {
		ra.addFlashAttribute("memberDetails", passiService.getMemberDetails(userID));
		ra.addFlashAttribute("selectedMember", userID);
		ra.addFlashAttribute("worksheetAnswers", passiService.getWorksheetAnswers(worksheetID, userID));
		ra.addFlashAttribute("worksheetContent", passiService.getWorksheetContent(worksheetID));
		logger.info("selectMember completed");
		return "redirect:/index";
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
	public String addGroup(
			@ModelAttribute("newGroup") Group newGroup,
			@ModelAttribute("userDetails") User instructor,
			final RedirectAttributes ra) {
		if (dao.addGroup(newGroup, instructor)) {  // fix to call via passiService
			ra.addFlashAttribute("message", "Ryhmän lisääminen onnistui.");
			ra.addFlashAttribute("newGroup", new Group());
		} else {
			ra.addFlashAttribute("message", "Ryhmän lisääminen EI onnistunut.");
		}
		return "redirect:/index/group";
	}

	@RequestMapping(value = "/delGroup", method = RequestMethod.POST)
	public String delGroup(@RequestParam int groupID, @ModelAttribute("groups") ArrayList<Group> groups,
			final RedirectAttributes redirectAttributes) {
		if (dao.delGroup(groupID)) {
			redirectAttributes.addFlashAttribute("message", "Ryhmän poistaminen onnistui.");
		} else {
			redirectAttributes.addFlashAttribute("message", "Ryhmän poistaminen EI onnistunut.");
		}
		return "redirect:/index/group";
	}
	
	@RequestMapping(value = "/groupInfo", method = RequestMethod.GET)
	@ResponseBody
	public Group getGroupInfo(@RequestParam int groupID) {
		return dao.getGroup(groupID);
	}
	
	@RequestMapping(value = "/groupInfoUsers", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getGroupInfoWithUsers(@RequestParam int groupID) {
		Map<String, Object> group = new HashMap<String, Object>();
		group.put("group", dao.getGroup(groupID));
		group.put("users", dao.getGroupMembers(groupID));
		return group;
	}
	
	@RequestMapping(value = "/editGroup", method = RequestMethod.POST)
	public String editGroup(@ModelAttribute("editedGroup") Group editedGroup, final RedirectAttributes ra) {
		if (dao.editGroup(editedGroup)) {
			ra.addFlashAttribute("message", "Ryhmän muokkaus onnistui.");
			ra.addFlashAttribute("editedGroup", new Group());
		} else {
			ra.addFlashAttribute("message", "Ryhmän muokkaus EI onnistunut.");
		}
		return "redirect:/index/group";
	}
	
	@RequestMapping(value = "/delGroupMember", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> delGroupMember(
			@RequestParam(value="userID", required = true) int userID,
			@RequestParam(value = "groupID", required = true) int groupID) {
		Map<String, Boolean> status = new HashMap<>();
		status.put("status", dao.delGroupMember(userID, groupID));
		return status;
	}
	
	public String getAuthUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
}