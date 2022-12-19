package com.unibuc.validation.phonenumber;

import com.unibuc.validation.phonenumber.PhoneNumberMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberMatchValidation implements ConstraintValidator<PhoneNumberMatch, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)
            return false;
        return value.matches("^\\d{10}$");
    }
}
