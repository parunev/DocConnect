package com.parunev.docconnect.services;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.Status;
import com.parunev.docconnect.models.payloads.appointment.AppointmentRequest;
import com.parunev.docconnect.models.payloads.appointment.AppointmentResponse;
import com.parunev.docconnect.models.payloads.specialist.SpecialistAddressResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.AppointmentRepository;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.AppointmentDeniedException;
import com.parunev.docconnect.security.exceptions.AppointmentNotFoundException;
import com.parunev.docconnect.security.exceptions.SpecialistNotFoundException;
import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import com.parunev.docconnect.security.payload.ApiError;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.validators.AppointmentHelpers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.parunev.docconnect.security.SecurityUtils.getCurrentUserDetails;

/**
 * The {@code AppointmentService} class provides business logic and services related to appointments in the DocConnect
 * application. It handles operations such as creating appointments, canceling appointments, and searching for upcoming
 * and completed appointments with various filtering options.
 *
 * <p>This service class interacts with the {@link com.parunev.docconnect.repositories.AppointmentRepository},
 * {@link com.parunev.docconnect.repositories.UserRepository}, and {@link com.parunev.docconnect.repositories.SpecialistRepository}
 * repositories to perform data access operations related to appointments, users, and specialists.
 *
 * <p>The class is designed to be used in the context of managing appointments within the DocConnect application.
 */
@Service
@Validated
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final SpecialistRepository specialistRepository;
    private final AppointmentHelpers appointmentHelpers;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final DCLogger dcLogger = new DCLogger(AppointmentService.class);

    /**
     * Creates a new appointment based on the provided {@code AppointmentRequest}.
     *
     * @param request The appointment request containing user, specialist, and appointment details.
     * @return An {@link AppointmentResponse} representing the created appointment.
     * @throws SpecialistNotFoundException if the specialist specified in the request is not found.
     * @throws AppointmentDeniedException if the appointment request is denied due to validation or other criteria.
     * @throws UserNotFoundException if the user making the appointment request is not found.
     */
    public AppointmentResponse createAppointment(@Valid AppointmentRequest request){
        User user = findBySecurityContextHolder();
        dcLogger.info("User found successfully: {}", user.getEmail());

        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(() -> {
                    dcLogger.warn("Specialist not found");
                    return new SpecialistNotFoundException(AuthenticationError.builder()
                            .path(appointmentHelpers.getRequest().getRequestURI())
                            .error("Specialist not found")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
        dcLogger.info("Specialist found successfully: {}", specialist.getFirstName() + " " + specialist.getLastName());

        appointmentHelpers.validateAppointmentRequest(request,user,specialist);

        Appointment appointment = Appointment.builder()
                .user(user)
                .specialist(specialist)
                .appointmentStatus(Status.STATUS_UPCOMING)
                .dateTime(request.getDateTime())
                .build();
        appointmentRepository.save(appointment);
        dcLogger.info("Appointment verified and saved. Appointment: {}", appointment.getId());

        return AppointmentResponse.builder()
                .appointmentId(appointment.getId())
                .date(appointment.getDateTime().toLocalDate())
                .time(appointment.getDateTime().toLocalTime())
                .specialistName(specialist.getFirstName() + " " + specialist.getLastName())
                .specialistAddress(specialist.getAddresses().stream()
                        .map(address -> modelMapper.map(address, SpecialistAddressResponse.class))
                        .toList())
                .build();
    }

    /**
     * Cancels an existing appointment with the specified ID.
     *
     * @param appointmentId The ID of the appointment to be canceled.
     * @return An {@link AppointmentResponse} indicating the success of the cancellation.
     * @throws AppointmentNotFoundException if the appointment with the specified ID is not found.
     * @throws AppointmentDeniedException if the appointment is already canceled.
     */
    public AppointmentResponse cancelAppointment(Long appointmentId){
        dcLogger.info("Cancelling appointment with ID: {}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    dcLogger.warn("Appointment with ID {} not found.", appointmentId);
                    throw new AppointmentNotFoundException(ApiError.builder()
                            .path(appointmentHelpers.getRequest().getRequestURI())
                            .error("Appointment not found")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });

        if (appointment.getAppointmentStatus().equals(Status.STATUS_CANCELED)){
            dcLogger.warn("Appointment with id: {} is already canceled", appointment.getId());
            throw new AppointmentDeniedException(ApiError.builder()
                    .path(appointmentHelpers.getRequest().getRequestURI())
                    .error("Appointment already canceled")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } else {
            appointment.setAppointmentStatus(Status.STATUS_CANCELED);
            appointmentRepository.save(appointment);
            notificationService.sendAppointmentCanceledEmail(appointment);
        }

        return AppointmentResponse.builder()
                .message("The appointment was successfully canceled!")
                .build();
    }

    /**
     * Searches for upcoming appointments based on filtering criteria and pagination options.
     *
     * @param specialistName The name of the specialist to filter by (can be null).
     * @param specialtyId The ID of the specialty to filter by (can be null).
     * @param fromDate The start date of the date range to filter by (can be null).
     * @param toDate The end date of the date range to filter by (can be null).
     * @param pageable The pagination options.
     * @return A {@link Page} of {@link AppointmentResponse} objects representing the upcoming appointments that match the criteria.
     */
    public Page<AppointmentResponse> searchUpcomingAppointments(String specialistName, Long specialtyId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        User user = findBySecurityContextHolder();
        updateUserAppointments(user);

        Page<Appointment> upcomingAppointments = appointmentRepository
                .findUpcomingAppointments(
                        specialistName,
                        specialtyId,
                        fromDate,
                        toDate,
                        user.getId(),
                        pageable
                );

        return upcomingAppointments.map(this::mapToDto);
    }

    /**
     * Searches for completed appointments based on filtering criteria and pagination options.
     *
     * @param specialistName The name of the specialist to filter by (can be null).
     * @param specialtyId The ID of the specialty to filter by (can be null).
     * @param fromDate The start date of the date range to filter by (can be null).
     * @param toDate The end date of the date range to filter by (can be null).
     * @param pageable The pagination options.
     * @return A {@link Page} of {@link AppointmentResponse} objects representing the completed appointments that match the criteria.
     */
    public Page<AppointmentResponse> searchCompletedAppointments(String specialistName, Long specialtyId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        User user = findBySecurityContextHolder();
        updateUserAppointments(user);

        Page<Appointment> appointmentsPage = appointmentRepository.findCompletedAppointments(specialistName,
                specialtyId,
                fromDate,
                toDate,
                user.getId(),
                pageable);

        return appointmentsPage.map(this::mapToDto);
    }

    /**
     * Updates the status of appointments for the specified user, marking them as completed if the appointment
     * date and time are in the past.
     *
     * @param user The user for whom appointments are updated.
     */
    private void updateUserAppointments(User user) {
        List<Appointment> appointments = appointmentRepository.findAllByUserId(user.getId());
        if (appointments != null){
            dcLogger.info("Updating status of {} appointments for user: {}", appointments.size(), user.getEmail());

            appointments.stream()
                    .filter(a -> !a.getAppointmentStatus().equals(Status.STATUS_CANCELED))
                    .forEach(a -> {
                        LocalDateTime dateTime = LocalDateTime.of(a.getDateTime().toLocalDate(), a.getDateTime().toLocalTime());
                        if (LocalDateTime.now().isAfter(dateTime.plusHours(1))) {
                            a.setAppointmentStatus(Status.STATUS_COMPLETED);
                            appointmentRepository.save(a);
                        }
                    });
        }
    }

    /**
     * Retrieves the user based on the current security context information.
     *
     * @return The user retrieved from the security context.
     * @throws UserNotFoundException if the user is not found in the security context.
     */
    private User findBySecurityContextHolder(){
        return userRepository.findByEmail(getCurrentUserDetails().getUsername())
                .orElseThrow(() -> {
                    dcLogger.warn("User not found");
                    return new UserNotFoundException(AuthenticationError.builder()
                            .path(appointmentHelpers.getRequest().getRequestURI())
                            .error("User not found")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }

    /**
     * Maps an {@link Appointment} entity to an {@link AppointmentResponse} DTO.
     *
     * @param appointment The appointment entity to be mapped.
     * @return An {@link AppointmentResponse} DTO representing the appointment details.
     */
    private AppointmentResponse mapToDto(Appointment appointment) {
        return AppointmentResponse
                .builder()
                .appointmentId(appointment.getId())
                .specialistAddress(appointment.getSpecialist()
                        .getAddresses()
                        .stream()
                        .map(a -> modelMapper.map(a, SpecialistAddressResponse.class))
                        .toList())
                .specialistName(String.format("%s %s", appointment.getSpecialist().getFirstName(), appointment.getSpecialist().getLastName()))
                .date(appointment.getDateTime().toLocalDate())
                .build();
    }
}
