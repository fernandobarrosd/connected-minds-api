package com.fernando.connected_minds_api.validation.validators;

import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.validation.constraints.LocalDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateFormatConstraintValidator implements ConstraintValidator<LocalDateFormat, String> {

    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(dateString, DateTimeFormatters.LOCAL_DATE_FORMATTER);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
