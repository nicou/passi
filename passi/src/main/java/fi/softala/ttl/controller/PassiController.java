/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import fi.softala.ttl.dao.PassiDAO;

@Controller
@Scope("session")
@SessionAttributes({ "user" })
public class PassiController {

	final static Logger logger = LoggerFactory.getLogger(PassiController.class);
	
	@Inject
	private PassiDAO dao;

	public PassiDAO getDao() {
		return dao;
	}

	public void setDao(PassiDAO dao) {
		this.dao = dao;
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView init() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginPage(@RequestParam(value = "error", required = false) String error,
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

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView homePage() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		model.addObject("role", dao.getRole(user));
		model.addObject("user", user);
		model.setViewName("index");
		return model;
	}

	@RequestMapping(value = "/expired", method = RequestMethod.GET)
	public ModelAndView expiredPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("expired");
		return model;
	}

	// Upload single file and store locally
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody ModelAndView uploadFileHandler(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) {
		ModelAndView model = new ModelAndView();
		String feedback = "";
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				// Create the directory
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("Server File Location=" + serverFile.getAbsolutePath());
				feedback = "You successfully uploaded file " + name;
			} catch (Exception e) {
				feedback = "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			feedback = "You failed to upload " + name + " because the file was empty";
		}
		model.addObject("feedback", feedback);
		model.setViewName("index");
		return model;
	}
}