package com.fernando.connected_minds_api.validation.validators;

import com.fernando.connected_minds_api.validation.constraints.Enum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import java.util.Arrays;
import java.util.List;

public class EnumConstraintValidator implements ConstraintValidator<Enum, String> {
    List<String> enumValues;

    @Override
    public void initialize(Enum constraintAnnotation) {
        enumValues = Arrays.stream(constraintAnnotation.value())
                .toList();
    }

    @SneakyThrows
    @Override
    public boolean isValid(String enumValue, ConstraintValidatorContext context) {
        return enumValues.contains(enumValue);
    }
}