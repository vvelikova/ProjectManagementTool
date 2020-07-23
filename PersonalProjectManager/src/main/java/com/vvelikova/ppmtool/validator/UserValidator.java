package com.vvelikova.ppmtool.validator;

import com.vvelikova.ppmtool.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        //validate that we have a correct object here
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // cast the object to type User
        User user = (User)o;
        if(user.getPassword().length() < 6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }

        // make sure that password and confirmPassword match
        if(!user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "Match", "Password must match confirmPassword");
        }
    }
}
