package com.parunev.docconnect.utils.annotations.swagger.rating;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        description = "POST method to create and leave a Rating for a specific specialist. The specialist's ID is " +
                "provided on the path of the endpoint. The actual rating and comment is passed on the Request Body. ",
        summary = "Creates a new Rating and Comment for a Doctor.",
        responses = {
                @ApiResponse(
                        description = "The average Rating of that Specialist is returned.",
                        responseCode = "200"
                ),
                @ApiResponse(
                        description = "User or Specialist was not found.",
                        responseCode = "400"
                ),
        }
)
public @interface ApiCreateRating {
}
