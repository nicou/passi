package fi.softala.ttl.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

@ControllerAdvice
public class ExceptionHandlerController {
	
	final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@ExceptionHandler({Exception.class, RuntimeException.class, NestedServletException.class, SQLException.class})
	public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
		logger.error(e.toString() + ", request url: " + request.getRequestURI());
		return new ModelAndView("error");
	}
	
}
