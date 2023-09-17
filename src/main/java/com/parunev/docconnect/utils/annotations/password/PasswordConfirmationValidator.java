package com.parunev.docconnect.utils.annotations.password;

import com.parunev.docconnect.models.payloads.user.profile.PasswordChangeRequest;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator for confirming passwords in registration and password change requests.
 */
public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, Object> {

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation The annotation instance, not used in this case.
     */
    @Override
    public void initialize(PasswordConfirmation constraintAnnotation) {
         // This method is intentionally empty.
    }

    /**
     * Validates whether the password and its confirmation match.
     *
     * @param obj     The object to validate.
     * @param context The validation context.
     * @return True if the password and confirmation match, or if the object is null; otherwise, false.
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof RegistrationRequest yourClass) {
            String password = yourClass.getPassword();
            String confirmPassword = yourClass.getConfirmPassword();

            return password != null && password.equals(confirmPassword);
        } else if (obj instanceof PasswordChangeRequest yourClass) {
            String password = yourClass.getNewPassword();
            String confirmPassword = yourClass.getConfirmNewPassword();

            return password != null && password.equals(confirmPassword);
        }

        return false;
    }
}
