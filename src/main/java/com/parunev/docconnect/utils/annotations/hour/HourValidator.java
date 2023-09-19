package com.parunev.docconnect.utils.annotations.hour;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

/**
 * The {@code HourValidator} class is a custom constraint validator used for validating whether a {@link LocalDateTime}
 * value represents a time with a minute value of 0, indicating a round hour (e.g., 11:00, 9:00).
 *
 * <p>This validator is typically used in conjunction with the {@link RoundHour} annotation to ensure that date and time
 * values meet specific hour-rounding requirements.
 *
 * @see RoundHour
 * @see LocalDateTime
 * @see jakarta.validation.ConstraintValidator
 */
public class HourValidator implements ConstraintValidator<RoundHour, LocalDateTime> {

    /**
     * Initializes the constraint validator.
     *
     * @param constraintAnnotation The annotation instance used to initialize the validator.
     */
    @Override
    public void initialize(RoundHour constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates whether the given {@link LocalDateTime} value represents a round hour.
     *
     * @param value The {@link LocalDateTime} value to validate.
     * @param context The validation context.
     * @return {@code true} if the value represents a round hour (minute value is 0), otherwise {@code false}.
     */
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.getMinute() == 0;
    }
}
