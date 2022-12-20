package com.unibuc.main.validation.phonenumber;


import com.unibuc.main.constants.ProjectConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberMatchValidation.class)
public @interface PhoneNumberMatch {

    String message() default ProjectConstants.PHONE_NUMBER_MATCH;

    // represents groups of constraints
    Class<?>[] groups() default {};

    // represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
