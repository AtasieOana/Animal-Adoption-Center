package com.unibuc.validation.gender;

import com.unibuc.validation.phonenumber.PhoneNumberMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyCharacterMFValidation implements ConstraintValidator<OnlyCharacterMF, Character> {

    @Override
    public boolean isValid(Character value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)
            return false;
        return value == 'M' || value == 'F';
    }
}
