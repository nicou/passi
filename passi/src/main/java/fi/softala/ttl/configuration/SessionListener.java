package fi.softala.ttl.configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("=== Session started ===");
		event.getSession().setMaxInactiveInterval(30 * 60);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		System.out.println("=== Session destroy ===");
	}
}