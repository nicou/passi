package fi.softala.ttl.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestBridge {
	
	final static Logger logger = LoggerFactory.getLogger(RestBridge.class);
	
	public void updateRestPassword(int userID) {
		try {
			String request = "http://127.0.0.1:8080/passi-rest/update-rest-password/" + userID;
			URL url = new URL(request);
			URLConnection connection = url.openConnection();
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) builder.append(line);
			logger.info(builder.toString());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

}
