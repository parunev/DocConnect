package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.models.payloads.city.CityResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CityServiceException extends RuntimeException{
    private final transient CityResponse cityResponse;

    public CityServiceException(CityResponse cityResponse) {
        this.cityResponse = cityResponse;
    }
}
