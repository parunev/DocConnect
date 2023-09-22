package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.Status;
import com.parunev.docconnect.models.payloads.appointment.AppointmentRequest;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.AppointmentRepository;
import com.parunev.docconnect.security.exceptions.AppointmentDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AppointmentHelpersTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AppointmentHelpers appointmentHelpers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateAppointmentRequest_ValidRequest() {
        User user = new User();
        Specialist specialist = new Specialist();
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        // this needs to be changed or verified everytime on a CI/CD pipeline
        // not quite sure how to find a way around this
        appointmentRequest.setDateTime(LocalDateTime.of(2023,9,25,16, 0));

        when(appointmentRepository.findAllByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(appointmentRepository.findAllBySpecialistId(specialist.getId())).thenReturn(new ArrayList<>());

        appointmentHelpers.validateAppointmentRequest(appointmentRequest, user, specialist);
    }

    @Test
    void testValidateAppointmentRequest_SpecialistNotAvailable() {
        User user = new User();
        Specialist specialist = new Specialist();
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setDateTime(LocalDateTime.now());

        List<Appointment> specialistAppointments = new ArrayList<>();
        specialistAppointments.add(new Appointment());
        when(appointmentRepository.findAllBySpecialistId(specialist.getId())).thenReturn(specialistAppointments);

        assertThrows(AppointmentDeniedException.class, () -> appointmentHelpers.validateAppointmentRequest(appointmentRequest, user, specialist));
    }

    @Test
    void testValidateUserSpecialistAppointment_NoExistingAppointments() {
        Long userId = 1L;
        Long specialistId = 2L;
        LocalDate date = LocalDate.of(2023, 9, 21);

        when(appointmentRepository.findAllByUserIdAndSpecialistId(userId, specialistId)).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> appointmentHelpers.validateUserSpecialistAppointment(userId, specialistId, date));
    }

    @Test
    void testValidateUserSpecialistAppointment_ExistingAppointment() {
        Long userId = 1L;
        Long specialistId = 2L;
        LocalDate date = LocalDate.of(2023, 9, 21);

        List<Appointment> existingAppointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setDateTime(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()));
        appointment.setAppointmentStatus(Status.STATUS_UPCOMING);
        existingAppointments.add(appointment);

        when(appointmentRepository.findAllByUserIdAndSpecialistId(userId, specialistId)).thenReturn(existingAppointments);
        when(request.getRequestURI()).thenReturn("/api/v1/appointments");

        AppointmentDeniedException exception = assertThrows(AppointmentDeniedException.class,
                () -> appointmentHelpers.validateUserSpecialistAppointment(userId, specialistId, date));

        assertEquals("/api/v1/appointments", exception.getApiError().getPath());
        assertEquals("The user has already booked an appointment for the current doctor on that date.", exception.getApiError().getError());
        assertNotNull(exception.getApiError().getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getApiError().getStatus());
    }

    @Test
    void testValidateAppointmentExists_NoExistingAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        LocalDate date = LocalDate.of(2023, 9, 21);
        LocalTime time = LocalTime.of(14, 0);
        String errorMessage = "Appointment error message";

        when(appointmentRepository.findAllByUserId(anyLong())).thenReturn(appointments);

        assertDoesNotThrow(() -> appointmentHelpers.validateAppointmentExists(appointments, date, time, errorMessage));
    }

    @Test
    void testValidateDateTime_Weekend() {
        LocalDate weekendDate = LocalDate.of(2023, 9, 24); // A Saturday
        LocalTime validTime = LocalTime.of(10, 0); // Within working hours

        assertThrows(
                AppointmentDeniedException.class,
                () -> appointmentHelpers.validateDateTime(weekendDate, validTime)
        );
    }

    @Test
    void testValidateDateTime_DateNotInRange() {
        LocalDate futureDate = LocalDate.now().plusDays(33);
        LocalTime validTime = LocalTime.of(10, 0);

        assertThrows(
                AppointmentDeniedException.class,
                () -> appointmentHelpers.validateDateTime(futureDate, validTime)
        );
    }

}
