package com.parunev.docconnect.utils.annotations.hour;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The {@code RoundHour} annotation is a custom validation annotation used to enforce that a time represented by a
 *
 * <p>When this annotation is applied to a field, method parameter, or return value, the associated {@link HourValidator}
 * is triggered to perform the hour-rounding validation.
 *
 * <p>Example usage:
 * <pre>
 * &#064;RoundHour
 * private LocalDateTime appointmentTime;
 * </pre>
 */
@Documented
@Constraint(validatedBy = HourValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RoundHour {

    /**
     * Defines the default error message to be returned if the validation fails.
     *
     * @return The default error message.
     */
    String message() default "Time must be on the hour (e.g., 11:00, 9:00)";

    /**
     * Defines the groups to which this constraint belongs.
     *
     * @return An array of group classes.
     */
    Class<?>[] groups() default {};

    /**
     * Defines the payload types to which this constraint is targeted.
     *
     * @return An array of payload types.
     */
    Class<? extends Payload>[] payload() default {};
}
