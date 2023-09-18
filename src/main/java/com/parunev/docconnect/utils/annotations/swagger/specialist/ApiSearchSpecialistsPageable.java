package com.parunev.docconnect.utils.annotations.swagger.specialist;

import com.parunev.docconnect.models.payloads.specialist.SpecialistResponse;
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
@Operation(summary = "Get All Specialists By City Id/Name/Specialty Id",
        description = "Retrieve a page of all specialists by city id, name and specialty id.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200"
                , description = "Specialists retrieved successfully."
                , content = {@Content(mediaType = "application/json"
                , schema = @Schema(implementation = SpecialistResponse.class))})})
public @interface ApiSearchSpecialistsPageable {
}
