package com.parunev.docconnect.security.exceptions;

import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpecialtyServiceException extends RuntimeException{

        private final transient SpecialtyResponse specialtyResponse;

        public SpecialtyServiceException(SpecialtyResponse message) {
            this.specialtyResponse = message;
        }
}
