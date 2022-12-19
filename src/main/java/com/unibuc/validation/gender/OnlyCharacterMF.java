package com.unibuc.validation.gender;


import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.validation.phonenumber.PhoneNumberMatchValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyCharacterMFValidation.class)
public @interface OnlyCharacterMF {

    String message() default ProjectConstants.GENDER_MATCH;

    // represents groups of constraints
    Class<?>[] groups() default {};

    // represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
