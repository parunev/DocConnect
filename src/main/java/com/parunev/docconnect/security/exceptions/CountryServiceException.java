package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.models.payloads.country.CountryResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CountryServiceException extends RuntimeException{
    private final transient CountryResponse countryResponse;

    public CountryServiceException(CountryResponse message) {
        this.countryResponse = message;
    }
}
