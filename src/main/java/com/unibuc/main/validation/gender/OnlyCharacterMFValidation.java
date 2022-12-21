package com.unibuc.main.validation.gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyCharacterMFValidation implements ConstraintValidator<OnlyCharacterMF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)
            return false;
        return value.equals("M") || value.equals("F");
    }
}
