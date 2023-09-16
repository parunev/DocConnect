package com.parunev.docconnect.utils.annotations.swagger.auth;

import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        description = "POST endpoint to log in an already existing user to the application. This endpoint returns" +
                " two JWTs. An access token for the logged-in user to use for secure endpoints and a refresh token" +
                " to ask for a freshly made access token when it expires.",
        summary = "Logs in an existing user to the app. Returns a JWT access token and a JWT refresh token.",
        responses = {
                @ApiResponse(
                        description = "Success - User is logged in",
                        responseCode = "200",
                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}
                ),
                @ApiResponse(
                        description = "Unauthorized - Incorrect email or password",
                        responseCode = "401",
                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}
                ),
                @ApiResponse(
                        description = "Bad Request - Incorrectly formatted email address or password",
                        responseCode = "400"
                        ,content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}
                )
        }
)
public @interface ApiLogin {
}
