package com.unibuc.main.validation.gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyCharacterMFValidation implements ConstraintValidator<OnlyCharacterMF, Character> {

    @Override
    public boolean isValid(Character value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)
            return false;
        return value == 'M' || value == 'F';
    }
}
