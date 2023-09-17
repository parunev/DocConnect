package com.parunev.docconnect.utils.annotations.swagger.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Change user email", description = "PUT endpoint to change user email")
@ApiResponse(
        responseCode = "200"
        , description = "Changes the user email and returns the response with appropriate message."
        , content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.parunev.docconnect.models.payloads.user.profile.EmailChangeResponse.class))}
)
@ApiResponse(
        responseCode = "400"
        , description = "Email either is the same, is invalid or is already taken. Password might be invalid."
        , content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.parunev.docconnect.models.payloads.user.profile.EmailChangeResponse.class))}
)
@ApiResponse(
        responseCode = "404"
        , description = "The user profile was not found."
        ,content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.parunev.docconnect.models.payloads.user.profile.EmailChangeResponse.class))}
)
public @interface ApiChangeEmail {
}
