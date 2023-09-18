package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code AlreadyEnabledException} class is an exception used in a Spring-based application to indicate
 * that the user or the specialist is already enabled.
 * This exception extends the {@code RuntimeException} class and is annotated with
 * {@code @ResponseStatus} to specify that it should result in an HTTP response with a status code of "BAD REQUEST"
 * (HTTP 400).
 *
 * <p>When an instance of this exception is thrown, it includes an {@code AuthenticationError} object that provides
 * detailed information about the authentication error, including the error message, path, status code, and timestamp.
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyEnabledException extends RuntimeException{

    /**
     * The {@code AuthenticationError} object containing details about the authentication error.
     */
    private final transient AuthenticationError authenticationError;

    /**
     * Constructs a new {@code AlreadyEnabledException} with the specified {@code authenticationError}.
     *
     * @param authenticationError an {@code AuthenticationError} object that encapsulates information about
     *                            the authentication error.
     */
    public AlreadyEnabledException(AuthenticationError authenticationError) {
        this.authenticationError = authenticationError;
    }
}
