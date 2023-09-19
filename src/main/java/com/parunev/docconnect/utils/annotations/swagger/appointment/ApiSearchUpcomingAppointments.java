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
@Operation(summary = "Get All Upcoming Appointments By Specialist Name/Specialty Id/From Date/To Date",
        description = "Retrieve a page of all user upcoming appointments by specialist name, specialist Id, from date and to date.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200"
                , description = "Upcoming appointments retrieved successfully."
                , content = {@Content(mediaType = "application/json"
                , schema = @Schema(implementation = AppointmentResponse.class))})})
public @interface ApiSearchUpcomingAppointments {
}
