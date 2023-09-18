package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for representing specialist not found errors.
 * This exception is thrown when a specialist is not found in the system, typically during authentication or specialist-related operations.
 * It includes an authentication error message to provide additional details about the error.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpecialistNotFoundException extends RuntimeException{

    /**
     * The authentication error associated with the exception.
     */
    private final transient AuthenticationError error;

    public SpecialistNotFoundException(AuthenticationError message) {
        this.error = message;
    }
}
