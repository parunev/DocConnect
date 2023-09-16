package com.parunev.docconnect.utils.annotations.swagger.auth;

import com.parunev.docconnect.security.payload.LogoutResponse;
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
@Operation(summary = "Logout"
        , description = "This API endpoint is handled by the Security Configuration and it's invalidating the user JWT Tokens"
, tags = {"Authentication"}
, operationId = "logout"
, hidden = true)
@ApiResponse(
        responseCode = "200"
        , description = "User successfully logged out",
        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LogoutResponse.class))})
public @interface ApiLogout {
}
