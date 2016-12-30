package fi.softala.ttl.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class GroupKeyValidator {
	
	public boolean validate(String groupKey) {
		Pattern pattern = Pattern.compile("[a-zäöå]{4,20}");
		return pattern.matcher(groupKey).matches();
	}

}
