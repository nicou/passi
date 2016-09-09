package fi.softala.ttl.controller;

/**
 * @author Mika Ropponen
 */

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
import org.springframework.web.bind.annotation.SessionAttributes;
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
	public ModelAndView homePage() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		System.out.println(user);
		model.addObject("role", dao.getRole(user));
		model.addObject("user", user);
		model.setViewName("index");
		return model;
	}
}