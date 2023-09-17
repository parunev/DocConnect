package com.parunev.docconnect.utils.annotations.swagger.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Change user password", description = "PUT endpoint to change user password")
@ApiResponse(
        responseCode = "200"
        , description = "Changes the user password and returns the updated profile."
        , content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.parunev.docconnect.models.payloads.user.profile.PasswordChangeResponse.class))}
)
@ApiResponse(
        responseCode = "400"
        , description = "The request body contains invalid data."
        , content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.parunev.docconnect.models.payloads.user.profile.PasswordChangeResponse.class))}
)
@ApiResponse(
        responseCode = "404"
        , description = "The user profile was not found."
        ,content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.parunev.docconnect.models.payloads.user.profile.PasswordChangeResponse.class))}
)
public @interface ApiChangePassword {
}
