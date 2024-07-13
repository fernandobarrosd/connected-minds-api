package com.fernando.connected_minds_api.annotations.validators;

import com.fernando.connected_minds_api.annotations.EnumValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import java.util.Arrays;
import java.util.List;


public class EnumValidatorConstraint implements ConstraintValidator<EnumValidator, String> {
    List<String> enumValues;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        enumValues = Arrays.stream(constraintAnnotation.enumValues())
                .toList();
    }

    @SneakyThrows
    @Override
    public boolean isValid(String enumValue, ConstraintValidatorContext context) {
        return enumValues.contains(enumValue);
    }
}