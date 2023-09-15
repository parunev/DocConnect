package com.parunev.docconnect.utils.annotations.swagger.country;

import com.parunev.docconnect.models.payloads.country.CountryResponse;
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
@Operation(summary = "Get a list of all countries",
        description = "Get a list of all countries from the database",
        tags = {"Country Controller"},
        operationId = "getAllCountries")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "List of countries retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CountryResponse.class))
        ),
        @ApiResponse(responseCode = "500"
                , description = "Internal server error"
                , content = @Content(mediaType = "application/json"))
})
public @interface ApiGetAllCountries {
}
