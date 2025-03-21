package com.fernando.connected_minds_api.validation.annotations;

import com.fernando.connected_minds_api.validation.constraints.UsernamePatternConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Constraint(validatedBy = UsernamePatternConstraintValidator.class)
public @interface UsernamePattern {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}