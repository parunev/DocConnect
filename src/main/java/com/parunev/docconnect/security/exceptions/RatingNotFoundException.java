package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for representing rating not found errors.
 * This exception is thrown when a rating is not found in the system, typically during authentication or rating-related operations.
 * It includes an authentication error message to provide additional details about the error.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RatingNotFoundException extends RuntimeException{

    /**
     * The authentication error associated with the exception.
     */
    private final transient ApiError error;

    public RatingNotFoundException(ApiError message) {
        this.error = message;
    }
}
