package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code AppointmentNotFoundException} is a custom exception that indicates that an appointment was not found
 * in the DocConnect application. This exception is typically thrown when attempting to access or retrieve
 * an appointment that does not exist, resulting in a "NOT_FOUND" HTTP response status.
 *
 * <p>The exception extends the standard {@link RuntimeException}, making it a runtime exception that can be thrown
 * when necessary in the application's codebase.
 *
 * <p>Similar to other custom exceptions in the application, this exception includes an
 * {@link com.parunev.docconnect.security.payload.ApiError} object as part of its state. This object provides
 * additional information about the error, such as an error message and details, to convey specific error information
 * to the client.
 *
 * <p>The {@code AppointmentNotFoundException} is typically used when a client requests an appointment that
 * doesn't exist in the system, and it helps provide a clear and structured error response.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AppointmentNotFoundException extends RuntimeException{

    /**
     * The {@link com.parunev.docconnect.security.payload.ApiError} object providing additional error information.
     */
    private final transient ApiError apiError;

    /**
     * Constructs an {@code AppointmentNotFoundException} with the provided {@code ApiError} message.
     *
     * @param message The {@code ApiError} message describing the reason for the appointment not being found.
     */
    public AppointmentNotFoundException(ApiError message) {
        this.apiError = message;
    }
}
