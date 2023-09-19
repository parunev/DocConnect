package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling cases where a user's full name is not found.
 * This exception is typically thrown when attempting to authenticate a user from Google.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FullNameNotFoundException extends RuntimeException{

    private final transient AuthenticationError error;

    /**
     * Constructs a new FullNameNotFoundException with the given error message.
     *
     * @param message The authentication error message associated with this exception.
     */
    public FullNameNotFoundException(AuthenticationError message) {
        this.error = message;
    }
}
