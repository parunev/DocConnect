package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for representing user not found errors.
 * This exception is thrown when a user is not found in the system, typically during authentication or user-related operations.
 * It includes an authentication error message to provide additional details about the error.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    /**
     * The authentication error associated with the exception.
     */
    private final transient AuthenticationError error;

    public UserNotFoundException(AuthenticationError message) {
        this.error = message;
    }
}
