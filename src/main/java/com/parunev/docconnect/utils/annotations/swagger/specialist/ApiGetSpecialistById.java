package com.parunev.docconnect.utils.annotations.swagger.specialist;

import com.parunev.docconnect.models.payloads.specialist.SpecialistResponse;
import com.parunev.docconnect.security.payload.AuthenticationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Get Specialists By Id",
        description = "Retrieve a specialists by Id.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200"
                , description = "Specialist by Id retrieved successfully."
                , content = {@Content(mediaType = "application/json"
                , schema = @Schema(implementation = SpecialistResponse.class))}),
        @ApiResponse(responseCode = "400"
                , description = "Specialist with current Id not found."
                , content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationError.class))})
})
public @interface ApiGetSpecialistById {
}
