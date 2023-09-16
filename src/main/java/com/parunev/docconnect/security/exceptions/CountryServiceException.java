package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.models.payloads.country.CountryResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code CountryServiceException} class is an exception used in a Spring-based application to indicate errors related to
 * country service operations. This exception extends the {@code RuntimeException} class and is annotated with {@code @ResponseStatus}
 * to specify that it should result in an HTTP response with a status code of "BAD REQUEST" (HTTP 400).
 *
 * <p>When an instance of this exception is thrown, it includes a {@code CountryResponse} object that provides detailed
 * information about the country service error, including the response message, status code, and timestamp.
 *
 * @see RuntimeException
 * @see ResponseStatus
 * @see CountryResponse
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CountryServiceException extends RuntimeException{

    /**
     * The {@code CountryResponse} object containing details about the country service error.
     */
    private final transient CountryResponse countryResponse;

    /**
     * Constructs a new {@code CountryServiceException} with the specified {@code countryResponse}.
     *
     * @param message a {@code CountryResponse} object that encapsulates information about the country service error.
     */
    public CountryServiceException(CountryResponse message) {
        this.countryResponse = message;
    }
}
