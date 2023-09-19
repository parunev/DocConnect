package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.security.payload.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code AppointmentDeniedException} is a custom exception that indicates a denial or rejection of an appointment
 * request in the DocConnect application's security context. This exception is typically thrown when there is an issue
 * with the appointment request, and it results in a "BAD_REQUEST" HTTP response status.
 *
 * <p>The exception extends the standard {@link RuntimeException}, making it a runtime exception that can be thrown
 * when necessary in the application's codebase.
 *
 * <p>The exception includes an {@link com.parunev.docconnect.security.payload.ApiError} object as part of its state,
 * which provides additional information about the error, such as an error message and details. This can be useful for
 * conveying specific error information to the client.
 *
 * <p>The {@code AppointmentDeniedException} is typically used in scenarios where an appointment request does not meet
 * certain security or validation criteria and needs to be rejected.
 *
 * <p>By throwing this exception, the application can provide a structured error response to the client, indicating
 * the reason for the appointment request denial.
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppointmentDeniedException extends RuntimeException {

    /**
     * The {@link com.parunev.docconnect.security.payload.ApiError} object providing additional error information.
     */
    private final transient ApiError apiError;

    /**
     * Constructs an {@code AppointmentDeniedException} with the provided {@code ApiError} message.
     *
     * @param message The {@code ApiError} message describing the reason for the appointment request denial.
     */
    public AppointmentDeniedException(ApiError message) {
        this.apiError = message;
    }
}
