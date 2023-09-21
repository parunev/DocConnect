package com.parunev.docconnect.service;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.models.enums.Status;
import com.parunev.docconnect.models.payloads.appointment.AppointmentRequest;
import com.parunev.docconnect.models.payloads.appointment.AppointmentResponse;
import com.parunev.docconnect.models.payloads.specialist.SpecialistAddressResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.models.specialist.SpecialistAddress;
import com.parunev.docconnect.repositories.AppointmentRepository;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.AppointmentDeniedException;
import com.parunev.docconnect.security.exceptions.AppointmentNotFoundException;
import com.parunev.docconnect.security.exceptions.SpecialistNotFoundException;
import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import com.parunev.docconnect.services.AppointmentService;
import com.parunev.docconnect.services.NotificationService;
import com.parunev.docconnect.utils.validators.AppointmentHelpers;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private AppointmentHelpers appointmentHelpers;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateAppointment_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/appointments");
        when(appointmentHelpers.getRequest()).thenReturn(httpServletRequest);

        Long specialistId = 1L;
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setSpecialistId(specialistId);
        appointmentRequest.setDateTime(LocalDateTime.now());

        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        User user = User.builder()
                .email(userEmail)
                .password("test")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        Specialist specialist = Specialist.builder()
                .firstName("Test")
                .lastName("TestLast")
                .build();
        List<SpecialistAddress> addresses = Arrays.asList(
                new SpecialistAddress("123 Main St"),
                new SpecialistAddress("456 Elm St"));
        specialist.setAddresses(addresses);
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        Appointment expectedAppointment = Appointment.builder()
                .user(user)
                .specialist(specialist)
                .appointmentStatus(Status.STATUS_UPCOMING)
                .dateTime(appointmentRequest.getDateTime())
                .build();

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> Appointment.builder()
                        .user(user)
                        .specialist(specialist)
                        .appointmentStatus(Status.STATUS_UPCOMING)
                        .dateTime(appointmentRequest.getDateTime())
                        .build());

        AppointmentResponse appointmentResponse = appointmentService.createAppointment(appointmentRequest);

        List<SpecialistAddressResponse> expectedAddresses = addresses.stream()
                .map(address -> modelMapper.map(address, SpecialistAddressResponse.class))
                .toList();

        assertNotNull(user);
        assertNotNull(specialist);
        assertNotNull(appointmentResponse);
        assertEquals(Status.STATUS_UPCOMING, expectedAppointment.getAppointmentStatus());
        assertNotNull(appointmentResponse.getSpecialistAddress());
        assertEquals(expectedAddresses.size(), appointmentResponse.getSpecialistAddress().size());
        verify(appointmentHelpers, times(1)).validateAppointmentRequest(appointmentRequest, user, specialist);
        verify(appointmentRepository, times(1)).save(expectedAppointment);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateAppointment_UserNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/appointments");
        when(appointmentHelpers.getRequest()).thenReturn(httpServletRequest);

        Long specialistId = 1L;
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setSpecialistId(specialistId);
        appointmentRequest.setDateTime(LocalDateTime.now());

        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                appointmentService.createAppointment(appointmentRequest));

        assertEquals("/api/v1/appointments", exception.getError().getPath());
        assertEquals("User not found", exception.getError().getError());
        assertNotNull(exception.getError().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getError().getStatus());

        verify(userRepository, times(1)).findByEmail(userEmail);
        verifyNoInteractions(specialistRepository);
        verifyNoInteractions(appointmentRepository);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateAppointment_SpecialistNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/appointments");
        when(appointmentHelpers.getRequest()).thenReturn(httpServletRequest);

        Long specialistId = 1L;
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setSpecialistId(specialistId);
        appointmentRequest.setDateTime(LocalDateTime.now());

        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        User user = User.builder()
                .email(userEmail)
                .password("test")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        SpecialistNotFoundException exception = assertThrows(SpecialistNotFoundException.class, () -> appointmentService.createAppointment(appointmentRequest));

        assertEquals("/api/v1/appointments", exception.getError().getPath());
        assertEquals("Specialist not found", exception.getError().getError());
        assertNotNull(exception.getError().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getError().getStatus());

        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(specialistRepository, times(1)).findById(specialistId);
        verifyNoInteractions(appointmentRepository);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCancelAppointment_Success() {
        Long appointmentId = 1L;
        Appointment appointment = Appointment.builder()
                .appointmentStatus(Status.STATUS_UPCOMING)
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentResponse response = appointmentService.cancelAppointment(appointmentId);
        assertEquals(Status.STATUS_CANCELED, appointment.getAppointmentStatus());
        assertEquals("The appointment was successfully canceled!", response.getMessage());
        verify(appointmentRepository, times(1)).save(appointment);
        verify(notificationService, times(1)).sendAppointmentCanceledEmail(appointment);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCancelAppointment_AppointmentNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/appointments");
        when(appointmentHelpers.getRequest()).thenReturn(httpServletRequest);

        Long appointmentId = 1L;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.cancelAppointment(appointmentId));

        assertEquals("/api/v1/appointments", exception.getApiError().getPath());
        assertEquals("Appointment not found", exception.getApiError().getError());
        assertEquals(HttpStatus.NOT_FOUND, exception.getApiError().getStatus());
        assertNotNull(exception.getApiError().getTimestamp());

        verify(appointmentRepository, never()).save(any(Appointment.class));
        verify(notificationService, never()).sendAppointmentCanceledEmail(any(Appointment.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCancelAppointment_AlreadyCanceled() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/appointments");
        when(appointmentHelpers.getRequest()).thenReturn(httpServletRequest);

        Long appointmentId = 1L;
        Appointment appointment = Appointment.builder()
                .appointmentStatus(Status.STATUS_CANCELED)
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentDeniedException exception = assertThrows(AppointmentDeniedException.class,
                () -> appointmentService.cancelAppointment(appointmentId));

        assertEquals("/api/v1/appointments", exception.getApiError().getPath());
        assertEquals("Appointment already canceled", exception.getApiError().getError());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getApiError().getStatus());
        assertNotNull(exception.getApiError().getTimestamp());

        verify(appointmentRepository, never()).save(appointment);
        verify(notificationService, never()).sendAppointmentCanceledEmail(appointment);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testSearchUpcomingAppointments() {
        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        User user = User.builder()
                .email(userEmail)
                .password("test")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        String specialistName = "Dr. Smith";
        Long specialtyId = 1L;
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(7);

        List<Appointment> mockAppointments = Arrays.asList(
                Appointment.builder()
                        .dateTime(LocalDateTime.of(fromDate.plusDays(1), LocalTime.NOON))
                        .user(user)
                        .specialist(Specialist.builder()
                                .addresses(Collections.singletonList(new SpecialistAddress("123 Main St")))
                                .firstName("Dr. Smith")
                                .lastName("Specialist")
                                .build())
                        .build(),

                Appointment.builder()
                        .dateTime(LocalDateTime.of(fromDate.plusDays(2), LocalTime.NOON))
                        .user(user)
                        .specialist(Specialist.builder()
                                .addresses(Collections.singletonList(new SpecialistAddress("123 Main St")))
                                .firstName("Dr. Smith")
                                .lastName("Specialist")
                                .build())
                        .build()
        );

        when(appointmentRepository.findUpcomingAppointments(
                eq(specialistName),
                eq(specialtyId),
                eq(fromDate),
                eq(toDate),
                eq(user.getId()),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(mockAppointments));

        Page<AppointmentResponse> resultPage = appointmentService.searchUpcomingAppointments(
                specialistName, specialtyId, fromDate, toDate, PageRequest.of(0, 10));

        assertEquals(mockAppointments.size(), resultPage.getContent().size());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testSearchCompletedAppointments() {
        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        User user = User.builder()
                .email(userEmail)
                .password("test")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        String specialistName = "Dr. Smith";
        Long specialtyId = 1L;
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(7);

        List<Appointment> mockAppointments = Arrays.asList(
                Appointment.builder()
                        .dateTime(LocalDateTime.of(fromDate.plusDays(1), LocalTime.NOON))
                        .user(user)
                        .specialist(Specialist.builder()
                                .addresses(Collections.singletonList(new SpecialistAddress("123 Main St")))
                                .firstName("Dr. Smith")
                                .lastName("Specialist")
                                .build())
                        .build(),

                Appointment.builder()
                        .dateTime(LocalDateTime.of(fromDate.plusDays(2), LocalTime.NOON))
                        .user(user)
                        .specialist(Specialist.builder()
                                .addresses(Collections.singletonList(new SpecialistAddress("123 Main St")))
                                .firstName("Dr. Smith")
                                .lastName("Specialist")
                                .build())
                        .build()
        );

        when(appointmentRepository.findCompletedAppointments(
                eq(specialistName),
                eq(specialtyId),
                eq(fromDate),
                eq(toDate),
                eq(user.getId()),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(mockAppointments));

        Page<AppointmentResponse> resultPage = appointmentService.searchCompletedAppointments(
                specialistName, specialtyId, fromDate, toDate, PageRequest.of(0, 10));

        assertEquals(mockAppointments.size(), resultPage.getContent().size());
    }
}

