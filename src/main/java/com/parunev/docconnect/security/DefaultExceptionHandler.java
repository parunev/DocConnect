package com.parunev.docconnect.security;

import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import com.parunev.docconnect.security.exceptions.*;
import com.parunev.docconnect.security.payload.ApiError;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.security.payload.ConstraintError;
import com.parunev.docconnect.security.payload.EmailError;
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

/**
 * The {@code DefaultExceptionHandler} class is responsible for globally handling exceptions
 * that may occur during API requests and providing appropriate error responses.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {
    private static final String ERROR = "error";

    /**
     * Handle exceptions of type {@code CountryServiceException}.
     * This method handles exceptions specific to the country service and returns the
     * corresponding error response.
     *
     * @param ex The {@code CountryServiceException} instance to handle.
     * @return A {@code ResponseEntity} containing the error response and HTTP status.
     */
    @ExceptionHandler(CountryServiceException.class)
    public ResponseEntity<CountryResponse> handleCountryServiceException(CountryServiceException ex) {
        return new ResponseEntity<>(ex.getCountryResponse(), ex.getCountryResponse().getStatus());
    }

    /**
     * Handle exceptions of type {@code CityServiceException}.
     * This method handles exceptions specific to the city service and returns the
     * corresponding error response.
     *
     * @param ex The {@code CityServiceException} instance to handle.
     * @return A {@code ResponseEntity} containing the error response and HTTP status.
     */
    @ExceptionHandler(CityServiceException.class)
    public ResponseEntity<CityResponse> handleCityServiceException(CityServiceException ex) {
        return new ResponseEntity<>(ex.getCityResponse(), ex.getCityResponse().getStatus());
    }


    /**
     * Handle exceptions of type {@code EmailSenderException}.
     * This method handles exceptions specific to the email sender and returns the
     * corresponding error response.
     *
     * @param ex The {@code EmailSenderException} instance to handle.
     * @return A {@code EmailError} containing the error response and HTTP status.
     */
    @ExceptionHandler(EmailSenderException.class)
    public ResponseEntity<EmailError> handleEmailSenderException(EmailSenderException ex) {
        return new ResponseEntity<>(ex.getEmailError(), ex.getEmailError().getStatus());
    }

    /**
     * Handle exceptions of type {@code EmailAlreadyExistsAuthenticationException}.
     * This method handles exceptions specific to the email already exists exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code EmailAlreadyExistsAuthenticationException} instance to handle.
     * @return A {@code AuthenticationError} containing the error response and HTTP status.
     */
    @ExceptionHandler(EmailAlreadyExistsAuthenticationException.class)
    public ResponseEntity<AuthenticationError> handleEmailAlreadyExistsAuthenticationException
            (EmailAlreadyExistsAuthenticationException ex) {
        return new ResponseEntity<>(ex.getAuthenticationError(), ex.getAuthenticationError().getStatus());
    }

    /**
     * Handle exceptions of type {@code OtpValidationException}.
     * This method handles exceptions specific to the OTP validation exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code OtpValidationException} instance to handle.
     * @return A {@code String} containing the error response and HTTP status.
     */
    @ExceptionHandler(OtpValidationException.class)
    public ResponseEntity<String> handleOtpValidationException(OtpValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle exceptions of type {@code InvalidEmailTokenException}.
     * This method handles exceptions specific to the invalid email token exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code InvalidEmailTokenException} instance to handle.
     * @return A {@code EmailError} containing the error response and HTTP status.
     */
    @ExceptionHandler(InvalidEmailTokenException.class)
    public ResponseEntity<EmailError> handleInvalidEmailTokenException(InvalidEmailTokenException ex) {
        return new ResponseEntity<>(ex.getEmailError(), ex.getEmailError().getStatus());
    }

    /**
     * Handle exceptions of type {@code AuthenticationException}.
     * This method handles exceptions specific to the authentication exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code AuthenticationException} instance to handle.
     * @return A {@code AuthenticationError} containing the error response and HTTP status.
     */
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<AuthenticationError> handleAuthenticationException(InvalidLoginException ex) {
        return new ResponseEntity<>(ex.getAuthenticationError(), ex.getAuthenticationError().getStatus());
    }

    /**
     * Handle exceptions of type {@code UserNotFoundException}.
     * This method handles exceptions specific to the user not found exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code UserNotFoundException} instance to handle.
     * @return A {@code AuthenticationError} containing the error response and HTTP status.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AuthenticationError> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getError(), ex.getError().getStatus());
    }

    /**
     * Handle exceptions of type {@code InvalidPasswordChangeException}.
     * This method handles exceptions specific to the invalid password change exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code InvalidPasswordChangeException} instance to handle.
     * @return A {@code AuthenticationError} containing the error response and HTTP status.
     */
    @ExceptionHandler(InvalidPasswordChangeException.class)
    public ResponseEntity<AuthenticationError> handleInvalidPasswordChangeException(InvalidPasswordChangeException ex) {
        return new ResponseEntity<>(ex.getAuthenticationError(), ex.getAuthenticationError().getStatus());
    }

    /**
     * Handle exceptions of type {@code SpecialtyServiceException}.
     * This method handles exceptions specific to the specialty service and returns the
     * corresponding error response.
     *
     * @param ex The {@code SpecialtyServiceException} instance to handle.
     * @return A {@code ResponseEntity} containing the error response and HTTP status.
     */
    @ExceptionHandler(SpecialtyServiceException.class)
    public ResponseEntity<SpecialtyResponse> handleSpecialtyServiceException(SpecialtyServiceException ex) {
        return new ResponseEntity<>(ex.getSpecialtyResponse(), ex.getSpecialtyResponse().getStatus());
    }

    /**
     * Handle exceptions of type {@code SpecialistNotFoundException}.
     * This method handles exceptions specific to the specialist not found exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code SpecialistNotFoundException} instance to handle.
     * @return A {@code AuthenticationError} containing the error response and HTTP status.
     */
    @ExceptionHandler(SpecialistNotFoundException.class)
    public ResponseEntity<AuthenticationError> handleSpecialistNotFoundException(SpecialistNotFoundException ex) {
        return new ResponseEntity<>(ex.getError(), ex.getError().getStatus());
    }

    /**
     * Handle exceptions of type {@code AlreadyEnabledException}.
     * This method handles exceptions specific to the already enabled exception and returns the
     * corresponding error response.
     *
     * @param ex The {@code AlreadyEnabledException} instance to handle.
     * @return A {@code AuthenticationError} containing the error response and HTTP status.
     */
    @ExceptionHandler(AlreadyEnabledException.class)
    public ResponseEntity<AuthenticationError> handleAlreadyEnabledException(AlreadyEnabledException ex) {
        return new ResponseEntity<>(ex.getAuthenticationError(), ex.getAuthenticationError().getStatus());
    }

    /**
     * Handle exceptions of type {@code Exception} and {@code RuntimeException}.
     * This method handles all other exceptions and returns the corresponding error response.
     *
     * @param e       The {@code Exception} instance to handle.
     * @param request The HTTP servlet request associated with the exception.
     * @return A {@code ResponseEntity} containing the error response and HTTP status.
     */
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiError> handleException
            (Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(ApiError.builder()
                .path(request.getRequestURI())
                .error(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle exceptions of type {@code ConstraintViolationException}.
     * This method handles validation exceptions and constructs an error response
     * containing details about the validation errors.
     *
     * @param ex      The {@code ConstraintViolationException} instance to handle.
     * @param request The HTTP servlet request associated with the exception.
     * @return A {@code ResponseEntity} containing the error response with validation errors.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ConstraintError> handleValidationException(ConstraintViolationException ex, HttpServletRequest request) {
        // Extract validation errors and build a response with error details.
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

        // Return the response with validation error details and HTTP status.
        return new ResponseEntity<>(constraintError, HttpStatus.BAD_REQUEST);
    }
}
