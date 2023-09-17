package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for representing invalid password change requests.
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordChangeException extends RuntimeException{

    /**
     * The authentication error associated with the exception.
     */
    private final transient AuthenticationError authenticationError;

    /**
     * Constructs an instance of InvalidPasswordChangeException with the provided authentication error message.
     *
     * @param message The authentication error message.
     */
    public InvalidPasswordChangeException(AuthenticationError message) {
        this.authenticationError = message;
    }
}
