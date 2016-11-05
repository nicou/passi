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
import java.util.List;

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
import org.springframework.util.FileCopyUtils;
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
import fi.softala.ttl.dto.CategoryDTO;
import fi.softala.ttl.dto.GroupDTO;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.model.User;
import fi.softala.ttl.service.PassiService;

@EnableWebMvc
@Controller
@Scope("session")
@SessionAttributes({ "categories", "defaultGroup", "user", "groups", "groupMembers", "isAnsweredMap", "message", "newGroup", "newMember",
		"selectedCategory", "selectedGroup", "selectedMember", "selectedWorksheet", "worksheets" })
public class PassiController {

	final static Logger logger = LoggerFactory.getLogger(PassiController.class);

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
		return model;
	}

	// initiate session variables after login
	@RequestMapping(value = { "/init" }, method = RequestMethod.GET)
	public ModelAndView init(final RedirectAttributes redirectAttributes) {
		
		ModelAndView model = new ModelAndView();
		
		// session attributes for dropdown selection
		ArrayList<CategoryDTO> categories = new ArrayList<>();
		categories = passiService.getCategoriesDTO();
		redirectAttributes.addFlashAttribute("categories", categories);
		
		ArrayList<GroupDTO> groups = new ArrayList<>();
		groups = passiService.getGroupsDTO();
		redirectAttributes.addFlashAttribute("groups", groups);
		
		ArrayList<WorksheetDTO> worksheets = new ArrayList<>();
		redirectAttributes.addFlashAttribute("worksheets", worksheets);
		
		// session attributes for selections
		int selectedCategory = 0;
		redirectAttributes.addFlashAttribute("selectedCategory", selectedCategory);
		
		int selectedGroup = 0;
		redirectAttributes.addFlashAttribute("selectedGroup", selectedGroup);
		
		int selectedMember = 0;
		redirectAttributes.addFlashAttribute("selectedMember", selectedMember);
		
		int selectedWorksheet = 0;
		redirectAttributes.addFlashAttribute("selectedWorksheet", selectedWorksheet);
		
		redirectAttributes.addFlashAttribute("groupMembers", new ArrayList<User>());
		redirectAttributes.addFlashAttribute("newGroup", new Group());
		redirectAttributes.addFlashAttribute("newMember", new User());
		model.setViewName("redirect:/index");
		return model;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView homePage(
			@ModelAttribute("categories") ArrayList<CategoryDTO> categories,
			@ModelAttribute("groups") ArrayList<GroupDTO> groups,
			@ModelAttribute("selectedCategory") int selectedCategory,
			@ModelAttribute("selectedGroup") int selectedGroup,
			@ModelAttribute("selectedMember") int selectedMember,
			@ModelAttribute("selectedWorksheet") int selectedWorksheet,
			@ModelAttribute("worksheets") ArrayList<WorksheetDTO> worksheets,
			
			
			@ModelAttribute("groupMembers") List<User> groupMembers,
			@ModelAttribute("newGroup") Group newGroup,
			@ModelAttribute("newMember") User newMember) {
		ModelAndView model = new ModelAndView();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		
		// corrected
		model.addObject("groups", groups);
		model.addObject("categories", categories);
		model.addObject("selectedCategory", selectedCategory);
		model.addObject("selectedGroup", selectedGroup);
		model.addObject("selectedMember", selectedMember);
		model.addObject("selectedWorksheet", selectedWorksheet);
		
		// old ones
		model.addObject("groupMembers", groupMembers);
		model.addObject("message", "");
		model.addObject("newGroup", newGroup);
		model.addObject("newMember", newMember);
		model.addObject("worksheets", worksheets);
		model.addObject("user", user);
		model.setViewName("index");
		return model;
	}

	@RequestMapping(value = "/index/{page}", method = RequestMethod.GET)
	public ModelAndView pageNavigation(@PathVariable(value = "page") String page,
			@ModelAttribute(value = "message") String message) {
		ModelAndView model = new ModelAndView();
		//model.addObject("groups", dao.getAllGroups());
		model.addObject("message", message);
		model.setViewName(page);
		return model;
	}

	@RequestMapping(value = "/expired", method = RequestMethod.GET)
	public ModelAndView expiredPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("expired");
		return model;
	}

	// 1. SELECT GROUP
	@RequestMapping(value = "/selectGroup", method = RequestMethod.POST)
	public ModelAndView selectGroup(@RequestParam int groupID,
			@ModelAttribute("groups") ArrayList<GroupDTO> groups,
			@ModelAttribute("groupMembers") ArrayList<User> groupMembers) {
		ModelAndView model = new ModelAndView();
		groupMembers.clear();
		model.addObject("groups", groups);
		model.addObject("groupMembers", groupMembers);
		model.addObject("selectedCategory", 0);
		model.addObject("selectedGroup", groupID);
		model.addObject("selectedMember", 0);
		model.addObject("selectedWorksheet", 0);
		model.setViewName("index");
		return model;
	}
	
	// 2. SELECT CATEGORY
	@RequestMapping(value = "/selectCategory", method = RequestMethod.POST)
	public ModelAndView selectCategory(@RequestParam int categoryID,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("worksheets") ArrayList<WorksheetDTO> worksheets,
			@ModelAttribute("groupMembers") ArrayList<User> groupMembers) {		
		ModelAndView model = new ModelAndView();
		groupMembers.clear();
		model.addObject("groupMembers", groupMembers);
		model.addObject("selectedCategory", categoryID);
		model.addObject("selectedWorksheet", 0);
		model.addObject("worksheets", passiService.getWorksheetsDTO(groupID, categoryID));
		model.setViewName("index");
		return model;
	}
	
	// 3. SELECT WORKSHEET
	@RequestMapping(value = "/selectWorksheet", method = RequestMethod.POST)
	public ModelAndView selectWorksheet(@RequestParam int worksheetID,
			@ModelAttribute("groupMembers") ArrayList<User> groupMembers,
			@ModelAttribute("selectedGroup") int groupID,
			@ModelAttribute("selectedCategory") int categoryID) {		
		ModelAndView model = new ModelAndView();
		model.addObject("selectedWorksheet", worksheetID);
		if (groupMembers.isEmpty()) {
			groupMembers = passiService.getGroupMembers(groupID);
			model.addObject("groupMembers", groupMembers);
			model.addObject("isAnsweredMap", passiService.getIsAnsweredMap(worksheetID, groupMembers));
		}
		model.addObject("selectedMember", 0);
		model.setViewName("index");
		return model;
	}

	// 4. SELECT MEMBER (GET WORKSHEET WITH POSSIBLE ANSWERS)
	@RequestMapping(value = "/selectMember", method = RequestMethod.POST)
	public ModelAndView selectMember(@RequestParam int userID,
			@ModelAttribute("selectedWorksheet") int worksheetID) {		
		ModelAndView model = new ModelAndView();
		model.addObject("selectedMember", userID);
		model.addObject("worksheetAnswers", passiService.getWorksheetAnswers(worksheetID, userID));
		model.addObject("worksheetContent", passiService.getWorksheetContent(worksheetID));
		model.setViewName("index");
		return model;
	}

	@RequestMapping(value = "/saveWaypointFeedback", method = RequestMethod.POST)
	public ModelAndView saveWaypointFeedback(
			@RequestParam String waypointID,
			@RequestParam String instructorComment,
			@RequestParam String instructorRating) {
		ModelAndView model = new ModelAndView();
		passiService.saveFeadback(Integer.parseInt(waypointID), Integer.parseInt(instructorRating), instructorComment);
		model.setViewName("index");
		return model;
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

	/*
	 * @RequestMapping(value = "/editGroup", method = RequestMethod.POST) public
	 * ModelAndView editGroup(
	 * 
	 * @RequestParam String groupID,
	 * 
	 * @RequestParam String groupName,
	 * 
	 * @ModelAttribute("groups") ArrayList<Group> groups ) { ModelAndView model
	 * = new ModelAndView(); if (dao.editGroup(groupID, groupName)) {
	 * model.addObject("message", "Ryhmän muokkaus onnistui."); } else {
	 * model.addObject("message", "Ryhmän muokkaus EI onnistunut."); }
	 * model.addObject("groups", dao.getGroups()); model.setViewName("group");
	 * return model; }
	 * 
	 * @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
	 * public ModelAndView addStudent(
	 * 
	 * @ModelAttribute("message") String message,
	 * 
	 * @ModelAttribute("newStudent") User newStudent,
	 * 
	 * @RequestParam("groupID") String groupID) { ModelAndView model = new
	 * ModelAndView(); if (dao.addStudent(newStudent, groupID)) {
	 * model.addObject("message", "Opiskelijan lisääminen onnistui."); } else {
	 * model.addObject("message", "Opiskelijan lisääminen EI onnistunut."); }
	 * newStudent.reset(); model.addObject("groups", dao.getGroups());
	 * model.addObject("newStudent", newStudent); model.addObject("selectedTab",
	 * ""); model.setViewName("student"); return model; }
	 * 
	 * @RequestMapping(value = "/delStudent", method = RequestMethod.POST)
	 * public ModelAndView delStudent(
	 * 
	 * @RequestParam String studentID,
	 * 
	 * @ModelAttribute("groups") ArrayList<Group> groups) { ModelAndView model =
	 * new ModelAndView(); if (dao.deleteStudent(studentID)) {
	 * model.addObject("message", "Oppilaan poistaminen onnistui."); } else {
	 * model.addObject("message", "Oppilaan poistaminen EI onnistunut."); }
	 * model.addObject("groups", dao.getGroups()); model.setViewName("student");
	 * return model; }
	 * 
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/upload", method = RequestMethod.POST) public
	 * ModelAndView uploadFileHandler(
	 * 
	 * @RequestParam("name") String name,
	 * 
	 * @RequestParam("file") MultipartFile file, final RedirectAttributes ra) {
	 * ModelAndView model = new ModelAndView(); String message = ""; if
	 * (!file.isEmpty()) { try { byte[] bytes = file.getBytes(); File dir = new
	 * File(TOMCAT_IMG); if (!dir.exists()) { dir.mkdirs(); } File serverFile =
	 * new File(dir.getAbsolutePath() + File.separator + "image\\image.jpg");
	 * BufferedOutputStream stream = new BufferedOutputStream(new
	 * FileOutputStream(serverFile)); stream.write(bytes); stream.close();
	 * message = "Kuvan tallennus onnistui"; logger.info(
	 * "Server File Location: " + serverFile.getAbsolutePath()); } catch
	 * (Exception e) { message = "Kuvan tallennus ei onnistunut"; } } else {
	 * message = "Kuvatiedosto oli tyhjä"; } ra.addFlashAttribute("message",
	 * message); model.setViewName("redirect:/index"); return model; }
	 * 
	 */
}