package com.parunev.docconnect.utils.annotations.uri;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;

/**
 * Custom validator for validating URI strings.
 */
public class UriValidator implements ConstraintValidator<IsValidUri, String> {

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation The annotation instance, not used in this case.
     */
    @Override
    public void initialize(IsValidUri constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


    /**
     * Validates whether a given string is a valid URI.
     *
     * @param value                    The URI string to validate.
     * @param constraintValidatorContext The validation context.
     * @return True if the string is a valid URI with a scheme and host; otherwise, false.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        try {
            URI uri = new URI(value);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
