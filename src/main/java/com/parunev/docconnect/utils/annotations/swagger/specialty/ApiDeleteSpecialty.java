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
@Operation(summary = "Delete specialty",
        description = "Delete a specialty by ID",
        tags = {"Specialty Controller"},
        operationId = "deleteSpecialty")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Specialty deleted successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SpecialtyResponse.class))
        ),
        @ApiResponse(responseCode = "404",
                description = "Specialty with such ID does not exist",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SpecialtyResponse.class))
        )
})
public @interface ApiDeleteSpecialty {
}
