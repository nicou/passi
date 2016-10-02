/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
import fi.softala.ttl.model.Member;

@EnableWebMvc
@Controller
@Scope("session")
@SessionAttributes({"user", "groups", "groupMembers", "message", "newGroup", "newMember", "selectedGroupObject", 
	"selectedMemberObject", "defaultGroup"})
public class PassiController {

	final static Logger logger = LoggerFactory.getLogger(PassiController.class);
	// private static final String TOMCAT_HOME_PROPERTY = "catalina.home";
	// private static final String TOMCAT_IMG = System.getProperty(TOMCAT_HOME_PROPERTY);
	
	@Autowired
	ServletContext context;
	
	@Inject
	private PassiDAO dao;

	public PassiDAO getDao() {
		return dao;
	}

	public void setDao(PassiDAO dao) {
		this.dao = dao;
	}

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView loginPage(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Tarkista tunnuksesi");
		}
		if (logout != null) {
			model.addObject("message", "Olet kirjautunut ulos");
		}
		model.setViewName("login");
		return model;
	}
	
	@RequestMapping(value = {"/init"}, method = RequestMethod.GET)
	public ModelAndView init(final RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView();
		redirectAttributes.addFlashAttribute("groups", dao.getAllGroups());
		redirectAttributes.addFlashAttribute("groupMembers", new ArrayList<Member>());
		redirectAttributes.addFlashAttribute("message", new String(""));
		redirectAttributes.addFlashAttribute("newGroup", new Group());
		redirectAttributes.addFlashAttribute("newMember", new Member());
		redirectAttributes.addFlashAttribute("selectedGroupObject", new Group());
		redirectAttributes.addFlashAttribute("selectedMemberObject", new Member());
		model.setViewName("redirect:/index");
		return model;
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView homePage(
			@ModelAttribute("groups") List<Group> groups,
			@ModelAttribute("groupMembers") List<Member> groupMembers,
			@ModelAttribute("message") String message,
			@ModelAttribute("newGroup") Group newGroup,
			@ModelAttribute("newMember") Member newMember,
			@ModelAttribute("selectedGroupObject") Group selectedGroupObject,
			@ModelAttribute("selectedMemberObject") Member selectedMemberObject) {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		if (selectedGroupObject.getGroupID() != 0) {
			groupMembers = dao.getGroupMembers(selectedGroupObject);
		}
		model.addObject("groups", groups);
		model.addObject("groupMembers", groupMembers);
		model.addObject("message", message);
		model.addObject("newGroup", newGroup);
		model.addObject("newMember", newMember);
		model.addObject("selectedGroupObject", selectedGroupObject);
		model.addObject("selectedMemberObject", selectedMemberObject);
		model.addObject("user", user);
		model.setViewName("index");
		return model;
	}
	
	@RequestMapping(value = "/index/{page}", method = RequestMethod.GET)
	public ModelAndView pageNavigation(
			@PathVariable(value = "page") String page) {
		ModelAndView model = new ModelAndView();		
		model.setViewName(page);
		return model;
	}

	@RequestMapping(value = "/expired", method = RequestMethod.GET)
	public ModelAndView expiredPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("expired");
		return model;
	}
	
	@RequestMapping(value = "/getGroupData", method = RequestMethod.POST)
	public ModelAndView getGroupData(
			@RequestParam int groupID,
			@ModelAttribute("selectedMemberObject") Member member,
			@ModelAttribute("groups") List<Group> groups) {
		ModelAndView model = new ModelAndView();
		for (Group g : groups) {
			if (g.getGroupID() == groupID) {
				model.addObject("selectedGroupObject", g);
				model.addObject("groupMembers", dao.getGroupMembers(g));
				break;
			}
		}
		member.reset();
		model.addObject("selectedMemberObject", member);
		model.setViewName("index");
		return model;
	}
	
	@RequestMapping(value = "/getMemberAnswers", method = RequestMethod.POST)
	public ModelAndView getMemberAnswers(
			@RequestParam int groupID,
			@RequestParam int userID,
			@ModelAttribute("groupMembers") List<Member> groupMembers,
			@ModelAttribute("selectedGroupObject") Group selectedGroupObject,
			@ModelAttribute("selectedMemberObject") Member selectedMemberObject) {
		ModelAndView model = new ModelAndView();
		for (Member member : groupMembers) {
			if (member.getUserID() == userID) {
				model.addObject("selectedMemberObject", member);
				model.addObject("answers", dao.getAnswers(selectedGroupObject, member));
				break;
			}
		}
		// model.addObject("groupMembers", dao.getGroupMembers(selectedGroupObject));
		model.addObject("selectedGroupObject", selectedGroupObject);
		model.addObject("worksheets", dao.getWorksheets(selectedGroupObject));
		model.setViewName("index");
		return model;
	}
	

	
	/*
	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public ModelAndView addGroup(@ModelAttribute("newGroup") Group newGroup) {
		ModelAndView model = new ModelAndView();
		if (dao.addGroup(newGroup)) {
			model.addObject("message", "Ryhmän lisääminen onnitui.");
		} else {
			model.addObject("message", "Ryhmän lisääminen EI onnistunut.");
		}
		newGroup.reset();
		model.addObject("newGroup", newGroup);
		model.addObject("groups", dao.getGroups());
		model.setViewName("group");
		return model;
	}
	
	@RequestMapping(value = "/editGroup", method = RequestMethod.POST)
	public ModelAndView editGroup(
			@RequestParam String groupID,
			@RequestParam String groupName,
			@ModelAttribute("groups") ArrayList<Group> groups ) {
		ModelAndView model = new ModelAndView();
		if (dao.editGroup(groupID, groupName)) {
			model.addObject("message", "Ryhmän muokkaus onnistui.");
		} else {
			model.addObject("message", "Ryhmän muokkaus EI onnistunut.");
		}
		model.addObject("groups", dao.getGroups());
		model.setViewName("group");
		return model;
	}
	
	@RequestMapping(value = "/delGroup", method = RequestMethod.POST)
	public ModelAndView delGroup(
			@RequestParam String groupID,
			@ModelAttribute("groups") ArrayList<Group> groups) {
		ModelAndView model = new ModelAndView();
		if (dao.deleteGroup(groupID)) {
			model.addObject("message", "Ryhmän poistaminen onnistui.");
		} else {
			model.addObject("message", "Ryhmän poistaminen EI onnistunut.");
		}
		model.addObject("groups", dao.getGroups());
		model.setViewName("group");
		return model;
	}
	
	@RequestMapping(value = "/addStudent", method = RequestMethod.POST)
	public ModelAndView addStudent(
			@ModelAttribute("message") String message,
			@ModelAttribute("newStudent") Member newStudent,
			@RequestParam("groupID") String groupID) {
		ModelAndView model = new ModelAndView();
		if (dao.addStudent(newStudent, groupID)) {
			model.addObject("message", "Opiskelijan lisääminen onnistui.");
		} else {
			model.addObject("message", "Opiskelijan lisääminen EI onnistunut.");
		}
		newStudent.reset();
		model.addObject("groups", dao.getGroups());
		model.addObject("newStudent", newStudent);
		model.addObject("selectedTab", "");
		model.setViewName("student");
		return model;
	}
	
	@RequestMapping(value = "/delStudent", method = RequestMethod.POST)
	public ModelAndView delStudent(
			@RequestParam String studentID,
			@ModelAttribute("groups") ArrayList<Group> groups) {
		ModelAndView model = new ModelAndView();
		if (dao.deleteStudent(studentID)) {
			model.addObject("message", "Oppilaan poistaminen onnistui.");
		} else {
			model.addObject("message", "Oppilaan poistaminen EI onnistunut.");
		}
		model.addObject("groups", dao.getGroups());
		model.setViewName("student");
		return model;
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(
			@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file,
			final RedirectAttributes ra) {
		ModelAndView model = new ModelAndView();
		String message = "";
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				File dir = new File(TOMCAT_IMG);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File serverFile = new File(dir.getAbsolutePath() + File.separator + "image\\image.jpg");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				message = "Kuvan tallennus onnistui";
				logger.info("Server File Location: " + serverFile.getAbsolutePath());
			}
			catch (Exception e) {
				message = "Kuvan tallennus ei onnistunut";
			}
		} else {
			message = "Kuvatiedosto oli tyhjä";
		}
		ra.addFlashAttribute("message", message);
		model.setViewName("redirect:/index");
		return model;
	}

	@RequestMapping(value = "/download/{type}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable("type") String type) throws IOException {
		File file = new File(EXTERNAL_IMG_FILE);
		if (!file.exists()) {
			String errorMessage = "Tiedostoa ei löydy";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			System.out.println("MIME tunnistamaton");
			mimeType = "application/octet-stream";
		}
		System.out.println("mimetype : " + mimeType);
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	*/
}