package com.parunev.docconnect.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code OtpValidationException} class is an exception used to signal OTP (One-Time Password)
 * validation failures in a Spring-based application. This exception extends the {@code RuntimeException}
 * class and is annotated with {@code @ResponseStatus} to indicate that it should result in an HTTP response
 * with a status code of "BAD REQUEST" (HTTP 400).
 *
 * <p>When an instance of this exception is thrown, it provides a message describing the reason for the OTP
 * validation failure. This message can be customized to provide details about the specific validation error.
 *
 * @see RuntimeException
 * @see ResponseStatus
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OtpValidationException extends RuntimeException {

    /**
     * Constructs a new {@code OtpValidationException} with the specified detail message.
     *
     * @param message a descriptive message indicating the reason for the OTP validation failure
     */
    public OtpValidationException(String message) {
        super(message);
    }
}
