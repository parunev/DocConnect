package com.parunev.docconnect.utils.annotations.swagger.specialty;

import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
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
@Operation(summary = "Get a specialty by ID",
        description = "Get a specialty by ID from the database",
        tags = {"Specialty Controller"},
        operationId = "getSpecialtyById")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Specialty retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SpecialtyResponse.class))
        ),
        @ApiResponse(responseCode = "404"
                , description = "Specialty with such ID does not exist"
                , content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "500"
                , description = "Internal server error"
                , content = @Content(mediaType = "application/json")
        )
})
public @interface ApiGetSpecialtyById {
}
