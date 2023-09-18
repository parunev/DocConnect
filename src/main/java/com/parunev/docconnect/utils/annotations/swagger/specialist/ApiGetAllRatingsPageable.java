package com.parunev.docconnect.utils.annotations.swagger.specialist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        description = "GET method to return all ratings and comments for the given doctor. The Doctor's ID is " +
                "provided on the endpoint's path. It returns a JSON Page containing all ratings and comments.",
        summary = "Gets all ratings and comments for a specific specialist.",
        responses = {
                @ApiResponse(
                        description = "A Page of Rating and Comments of a Doctor is returned",
                        responseCode = "200"
                ),
        }
)
public @interface ApiGetAllRatingsPageable {
}
