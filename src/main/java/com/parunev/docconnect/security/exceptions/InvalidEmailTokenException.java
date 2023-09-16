package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.EmailError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code InvalidEmailTokenException} class is an exception used in a Spring-based application to indicate
 * errors related to email token validation. This exception extends the {@code RuntimeException} class and is
 * annotated with {@code @ResponseStatus} to specify that it should result in an HTTP response with a status code
 * of "INTERNAL SERVER ERROR" (HTTP 500).
 *
 * <p>When an instance of this exception is thrown, it includes an {@code EmailError} object that provides detailed
 * information about the email token error, including the error message, path, status code, and timestamp.
 *
 * @see RuntimeException
 * @see ResponseStatus
 * @see EmailError
 */
@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidEmailTokenException extends RuntimeException{

    /**
     * The {@code EmailError} object containing details about the email token error.
     */
    private final transient EmailError emailError;

    /**
     * Constructs a new {@code InvalidEmailTokenException} with the specified {@code emailError}.
     *
     * @param message an {@code EmailError} object that encapsulates information about the email token error.
     */
    public InvalidEmailTokenException(EmailError message) {
            this.emailError = message;
        }
}
