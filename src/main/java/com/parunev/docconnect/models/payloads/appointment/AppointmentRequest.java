package com.parunev.docconnect.models.payloads.appointment;

import com.parunev.docconnect.utils.annotations.hour.RoundHour;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "Appointment Request", description = "The request payload to create a new appointment")
public class AppointmentRequest {

    @NotNull(message = "Please choose a specialist.")
    @Schema(description = "Represents the id of the specialist.", example = "1")
    private Long specialistId;

    @RoundHour
    @Schema(description = "Represents the date and the time of the appointment.", example = "2023-08-22T14:30:00")
    private LocalDateTime dateTime;
}
