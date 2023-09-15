package com.parunev.docconnect.utils.annotations.swagger.city;

import com.parunev.docconnect.models.payloads.city.CityResponse;
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
@Operation(summary = "Update city",
        description = "Update an existing city by Id and save it",
        tags = {"City Controller"},
        operationId = "updateCity")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "City updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CityResponse.class))
        ),
        @ApiResponse(responseCode = "400",
                description = "City not updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CityResponse.class))
        ),
        @ApiResponse(responseCode = "404",
                description = "City with such Id does not exists",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CityResponse.class))
        ),
        @ApiResponse(responseCode = "500"
                , description = "Internal server error"
                , content = @Content(mediaType = "application/json"))
})
public @interface ApiUpdateCity {
}
