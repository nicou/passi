package fi.softala.ttl.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fi.softala.ttl.model.User;

@Component
public class UserValidator implements Validator {
	
	/*
    @Autowired
    private UserService userService;
    */
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";  

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 20) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "NotEmpty");
        if (user.getFirstname().length() < 2 || user.getFirstname().length() > 40) {
            errors.rejectValue("firstname", "Size.userForm.firstname");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "NotEmpty");
        if (user.getLastname().length() < 2 || user.getLastname().length() > 60) {
            errors.rejectValue("lastname", "Size.userForm.lastname");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (!user.getEmail().matches(EMAIL_PATTERN)) {
            errors.rejectValue("email", "Match.userForm.email");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty");
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getConfirmPassword().equals(user.getPassword())) {
            errors.rejectValue("confirmPassword", "Diff.userForm.passwordConfirm");
        }

        /*
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }
        */
    }
}