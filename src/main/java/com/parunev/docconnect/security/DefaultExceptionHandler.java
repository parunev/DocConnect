package com.parunev.docconnect.security;

import com.parunev.docconnect.security.exceptions.CountryServiceException;
import com.parunev.docconnect.security.payload.ConstraintError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {
    private static final String ERROR = "error";

    @ExceptionHandler(CountryServiceException.class)
    public ResponseEntity<Object> handleCountryServiceException(CountryServiceException ex) {
        return new ResponseEntity<>(ex.getCountryResponse(), ex.getCountryResponse().getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ConstraintError> handleValidationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(error -> {
            String errorMessage = error.getMessage();
            errors.put(ERROR, errorMessage);
        });

        ConstraintError constraintError = ConstraintError
                .builder()
                .path(request.getRequestURI())
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(constraintError, HttpStatus.BAD_REQUEST);
    }
}
