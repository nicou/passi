package fi.softala.ttl.helper;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Emailer {
	
	final static Logger logger = LoggerFactory.getLogger(Emailer.class);
	
	@Value("${passi.protocol}")
	private String PROTOCOL;
	
	@Value("${passi.domain}")
	private String DOMAIN;
	
	public void sendPasswordResetMessage(String email, String token) {
		String host = "localhost";
		Properties props = System.getProperties();
		String resetUrl =  PROTOCOL + DOMAIN + "/passi/passrestore?token=" + token;
		
		props.put("mail.smtp.host", host);
		props.put("mail.debug", "false");
		
		Session session = Session.getInstance(props);
		
		MimeMessage message = new MimeMessage(session);
		
		try {
			message.setFrom(new InternetAddress("noreply@" + DOMAIN));
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
			message.setSubject("Työkykypassi - Salasanan palautus", "UTF-8");
			message.setContent("Voit vaihtaa salasanasi klikkaamalla alla olevaa linkkiä:"
					+ "<br /><br /><a href=\"" + resetUrl + "\">" + resetUrl + "</a>"
							+ "<br /><br />Linkki on voimassa 24 tuntia.", "text/html;charset=utf-8");
			
			Transport.send(message);
			logger.debug("Password reset link sent to " + email);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
