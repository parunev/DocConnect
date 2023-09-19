package com.parunev.docconnect.utils.annotations.swagger.appointment;

import com.parunev.docconnect.models.payloads.appointment.AppointmentResponse;
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
@Operation(summary = "Cancel an appointment",
        description = "POST endpoint for canceling an appointment")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200"
                , description = "Appointment canceled successfully."
                , content = {@Content(mediaType = "application/json"
                , schema = @Schema(implementation = AppointmentResponse.class))}),
        @ApiResponse(responseCode = "404"
                , description = """
                Status code 404 is returned due to:
                    * User is not found or unauthorized.
                    * Appointment not found.
                """
                , content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = AppointmentResponse.class)
        )})
})
public @interface ApiCancelAppointment {
}
