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
@Operation(summary = "Get city by country id",
        description = "Get city by country id from database",
        tags = {"City Controller"},
        operationId = "getCityByCountryId")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "City retrieved successfully from database",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CityResponse.class))
        ),
        @ApiResponse(responseCode = "400",
                description = "Invalid country id",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "404",
                description = "No cities found for specified country id",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "500"
                , description = "Internal server error"
                , content = @Content(mediaType = "application/json"))
})
public @interface ApiGetCityByCountryId {
}
