package com.parunev.docconnect.utils.annotations.swagger.rating;

import com.parunev.docconnect.models.payloads.rating.RatingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        description = "DELETE method to delete a Rating. The rating's ID is " +
                "provided on the path of the endpoint.",
        summary = "Delete a specific rating for a specialist.",
        responses = {
                @ApiResponse(
                        description = "The rating was delete successfully",
                        responseCode = "200",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponse.class))
                        }
                ),
                @ApiResponse(
                        description = "Rating not found.",
                        responseCode = "404",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponse.class))
                        }
                ),
        }
)
public @interface ApiDeleteRating {
}
