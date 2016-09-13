/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fi.softala.ttl.dao.PassiDAO;

@EnableWebMvc
@Controller
@Scope("session")
@SessionAttributes({"user"})
public class PassiController {

	final static Logger logger = LoggerFactory.getLogger(PassiController.class);
	private static final String TOMCAT_HOME_PROPERTY = "catalina.home";
	private static final String TOMCAT_IMG = System.getProperty(TOMCAT_HOME_PROPERTY);
	// private static final String EXTERNAL_IMG_FILE = "C:\\Users\\Mika\\Documents\\env\\apache-tomcat-8.0.36\\image\\image.jpg";
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

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ModelAndView init() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
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

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView homePage(@ModelAttribute("message") String message) throws IOException {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		model.addObject("message", message);
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

	/*
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