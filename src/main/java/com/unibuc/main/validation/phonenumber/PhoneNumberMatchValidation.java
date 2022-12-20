package com.unibuc.main.validation.phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberMatchValidation implements ConstraintValidator<PhoneNumberMatch, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)
            return false;
        return value.matches("^\\d{10}$");
    }
}
