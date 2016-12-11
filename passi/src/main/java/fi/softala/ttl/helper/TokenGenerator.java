package fi.softala.ttl.helper;

import java.security.SecureRandom;

public class TokenGenerator {
	
	public String generateToken() {
		String[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");
		String token = "";
		for (int i = 0; i < 64; i++) {
			SecureRandom rand = new SecureRandom();
			token += chars[rand.nextInt(chars.length)];
		}
		return token;
	}

}
