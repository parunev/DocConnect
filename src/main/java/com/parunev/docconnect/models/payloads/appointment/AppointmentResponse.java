package com.parunev.docconnect.models.payloads.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.parunev.docconnect.models.payloads.specialist.SpecialistAddressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Appointment Response", description = "The response payload of the appointment")
public class AppointmentResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Represents a server message", example = "Successfully canceled")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Represents the id of the appointment", example = "1")
    private Long appointmentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Represents the date of the appointment.", example = "2023-11-21")
    private LocalDate date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Represents the time of the appointment", example = "11:00:00")
    private LocalTime time;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Represents the full name of the specialist.", example = "Martin Parunev")
    private String specialistName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Represents the specialist's addresses.", example = "123 Main Street, CityA, CountryA")
    private List<SpecialistAddressResponse> specialistAddress;
}
