package com.fernando.connected_minds_api.validation.constraints;

import com.fernando.connected_minds_api.validation.annotations.UsernameSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameSizeConstraintValidator implements ConstraintValidator<UsernameSize, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username.length() >= 4;
    }
}