package com.fernando.connected_minds_api.validation.constraints;

import com.fernando.connected_minds_api.validation.annotations.UsernamePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernamePatternConstraintValidator implements ConstraintValidator<UsernamePattern, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username.matches("[A-Za-z0-9_]+");
    }
}
