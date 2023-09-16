package com.parunev.docconnect.utils.annotations.password;

import com.parunev.docconnect.models.payloads.user.registration.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, Object> {

    @Override
    public void initialize(PasswordConfirmation constraintAnnotation) {
         // This method is intentionally empty.
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof RegistrationRequest yourClass) {
            String password = yourClass.getPassword();
            String confirmPassword = yourClass.getConfirmPassword();

            return password != null && password.equals(confirmPassword);
        }

        return false;
    }
}
