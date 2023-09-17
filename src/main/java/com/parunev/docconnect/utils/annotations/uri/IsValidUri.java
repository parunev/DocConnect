package com.parunev.docconnect.utils.annotations.uri;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UriValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsValidUri {
    String message() default "Invalid URI";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
