package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.appointment.AppointmentRequest;
import com.parunev.docconnect.models.payloads.appointment.AppointmentResponse;
import com.parunev.docconnect.services.AppointmentService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.appointment.ApiCancelAppointment;
import com.parunev.docconnect.utils.annotations.swagger.appointment.ApiCreateAppointment;
import com.parunev.docconnect.utils.annotations.swagger.appointment.ApiSearchCompletedAppointments;
import com.parunev.docconnect.utils.annotations.swagger.appointment.ApiSearchUpcomingAppointments;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
@Schema(name = "Appointment Controller", description = "CRUD operations related to appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DCLogger dcLogger = new DCLogger(AppointmentController.class);

    @ApiCreateAppointment
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request){
        dcLogger.info("Request for creating a new appointment");
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @ApiCancelAppointment
    @PutMapping("/cancel/{appointmentId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable("appointmentId") Long appointmentId){
        dcLogger.info("Request for canceling an appointment");
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId));
    }

    @ApiSearchUpcomingAppointments
    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Page<AppointmentResponse>> searchUserUpcomingAppointments(
            @Parameter(description = "The name of the specialists for which to retrieve appointments.")
            @RequestParam(required = false, defaultValue = "") String specialistName,
            @Parameter(description = "The ID of the specialty for which to retrieve appointments.")
            @RequestParam(required = false, defaultValue = "") Long specialtyId,
            @Parameter(description = "The from date for which to retrieve appointments.")
            @RequestParam(required = false, defaultValue = "") LocalDate fromDate,
            @Parameter(description = "The to date for which to retrieve appointments.")
            @RequestParam(required = false, defaultValue = "") LocalDate toDate,
            Pageable pageable){
        dcLogger.info("Retrieving all upcoming appointments for the user." +
                "Specialist name: {}" +
                "Specialty Id: {}" +
                "From date: {}" +
                "To date: {}", specialistName, specialtyId, fromDate, toDate);

        return ResponseEntity.ok(appointmentService.searchUpcomingAppointments(
                specialistName, specialtyId, fromDate, toDate, pageable));
    }

    @ApiSearchCompletedAppointments
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Page<AppointmentResponse>> searchCompletedAppointment(
            @Parameter(description = "The name of the specialists for which to retrieve appointments.")
            @RequestParam(required = false, defaultValue = "") String specialistName,
            @Parameter(description = "The ID of the specialty for which to retrieve appointments.")
            @RequestParam(required = false, defaultValue = "") Long specialtyId,
            @Parameter(description = "The from date for which to retrieve them appointments.")
            @RequestParam(required = false, defaultValue = "") LocalDate fromDate,
            @Parameter(description = "The to date for which to retrieve specialists appointments.")
            @RequestParam(required = false, defaultValue = "") LocalDate toDate,
            Pageable pageable) {
        dcLogger.info("Retrieving all completed appointment for user." +
                        "Specialist name: {}" +
                        "Specialty Id: {}." +
                        "From date: {}," +
                        "To date: {}", specialistName, specialtyId, fromDate, toDate);

        return ResponseEntity.ok(appointmentService.searchCompletedAppointments(specialistName, specialtyId, fromDate, toDate, pageable));
    }
}
