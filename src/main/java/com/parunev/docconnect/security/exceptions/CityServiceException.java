package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.models.payloads.city.CityResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code CityServiceException} class is an exception used in a Spring-based application to indicate errors related to
 * city service operations. This exception extends the {@code RuntimeException} class and is annotated with {@code @ResponseStatus}
 * to specify that it should result in an HTTP response with a status code of "BAD REQUEST" (HTTP 400).
 *
 * <p>When an instance of this exception is thrown, it includes a {@code CityResponse} object that provides detailed
 * information about the city service error, including the response message, status code, and timestamp.
 *
 * @see RuntimeException
 * @see ResponseStatus
 * @see CityResponse
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CityServiceException extends RuntimeException{

    /**
     * The {@code CityResponse} object containing details about the city service error.
     */
    private final transient CityResponse cityResponse;

    /**
     * Constructs a new {@code CityServiceException} with the specified {@code cityResponse}.
     *
     * @param cityResponse a {@code CityResponse} object that encapsulates information about the city service error.
     */
    public CityServiceException(CityResponse cityResponse) {
        this.cityResponse = cityResponse;
    }
}
