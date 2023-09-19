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
@Operation(summary = "Schedule an appointment",
        description = "POST endpoint for scheduling an appointment")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200"
                , description = "Appointment scheduled successfully"
                , content = {@Content(mediaType = "application/json"
                , schema = @Schema(implementation = AppointmentResponse.class))}),
        @ApiResponse(responseCode = "400"
                , description = """
                Status code 400 is returned due to:
                    * User is not found or unauthorized.
                    * Specialist not found.
                    * User has already booked an appointment on this day and time for another specialist.
                    * User has already booked an appointment for the current specialist on that date.
                    * Current specialist is not available at the selected time.
                    * Specialists don't work during the weekend if the selected day is not a weekday.
                    * Selected time is out of the working day of the current specialist.
                    * Appointment time must be round hour from 9:00 - 16:00.
                    * User can't schedule an appointment in the past.
                    * No such status exists if database is down.
                    * User can't book an appointment on date later than one month from tomorrow.
                """
                , content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = AppointmentResponse.class)
        )})
})
public @interface ApiCreateAppointment {
}
