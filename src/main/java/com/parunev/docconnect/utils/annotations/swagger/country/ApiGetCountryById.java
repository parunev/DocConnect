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
@Operation(summary = "Get a country by ID",
        description = "Get a country by ID from the database",
        tags = {"Country Controller"},
        operationId = "getCountryById")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Country retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CountryResponse.class))
        ),
        @ApiResponse(responseCode = "404"
                , description = "Country with such ID does not exist"
                , content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "500"
                , description = "Internal server error"
                , content = @Content(mediaType = "application/json")
        )
})
public @interface ApiGetCountryById {
}
