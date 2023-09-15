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
@Operation(summary = "Add city",
        description = "Add city to database",
        tags = {"City Controller"},
        operationId = "addCity")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "City added successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CityResponse.class))
        ),
        @ApiResponse(responseCode = "400",
                description = "City with such name already exists",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CityResponse.class))
        ),
        @ApiResponse(responseCode = "500"
                , description = "Internal server error"
                , content = @Content(mediaType = "application/json"))
})
public @interface ApiAddCity {
}
