package com.parunev.docconnect.utils.annotations.swagger.specialty;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        description = "GET endpoint to retrieve all specialties available in the database. Returns a page of specialties " +
                "after a successful request. Returns 404 when the database is empty.",
        summary = "Gets all specialties available in the database",
        responses = {
                @ApiResponse(
                        description = "A Page of Specialties is returned",
                        responseCode = "200"
                ),
                @ApiResponse(
                        description = "Most likely due to an empty Specialty table in the DB",
                        responseCode = "404"
                )
        }
)
public @interface ApiSpecialtyPageable {
}
