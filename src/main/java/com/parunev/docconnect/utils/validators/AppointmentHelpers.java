package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.payloads.appointment.AppointmentRequest;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.AppointmentRepository;
import com.parunev.docconnect.security.exceptions.AppointmentDeniedException;
import com.parunev.docconnect.security.payload.ApiError;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class AppointmentHelpers {

    private final AppointmentRepository appointmentRepository;
    private final HttpServletRequest request;
    private final DCLogger dcLogger = new DCLogger(AuthHelpers.class);

    /**
     * Validates an appointment request, checking if it meets various criteria:
     * - The requested date and time are valid and fall within the specialist's working hours.
     * - The user has not already booked an appointment at the same date and time.
     * - The user does not already have an upcoming appointment with the same specialist on the same date.
     * - The specialist is available at the requested date and time.
     *
     * @param appointmentRequest The appointment request to be validated.
     * @param user               The user making the appointment.
     * @param specialist         The specialist being booked.
     * @throws AppointmentDeniedException If the appointment request is invalid or does not meet the criteria.
     */
    public void validateAppointmentRequest(AppointmentRequest appointmentRequest, User user, Specialist specialist) {
        dcLogger.info("Validating appointment request for user: {}, specialist: {}", user.getId(), specialist.getId());
        validateDateTime(appointmentRequest.getDateTime().toLocalDate(), appointmentRequest.getDateTime().toLocalTime());
        validateUserSpecialistAppointment(user.getId(), specialist.getId(), appointmentRequest.getDateTime().toLocalDate());
        validateUserAppointments(user, appointmentRequest);
        validateSpecialistAvailability(specialist, appointmentRequest);
    }

    private void validateSpecialistAvailability(Specialist specialist, AppointmentRequest appointmentRequest) {
        dcLogger.info("Validating specialist availability for appointment on date: {} and time: {}"
                , appointmentRequest.getDateTime().toLocalDate(), appointmentRequest.getDateTime().toLocalTime());

        validateAppointmentExists(
                appointmentRepository.findAllBySpecialistId(specialist.getId()),
                appointmentRequest.getDateTime().toLocalDate(),
                appointmentRequest.getDateTime().toLocalTime(),
                "Current specialist is not available, please choose another time."
        );

        dcLogger.info("Specialist availability validated");
    }

    private void validateUserAppointments(User user, AppointmentRequest appointmentRequest) {
        dcLogger.info("Validating user appointments for appointment on date: {} and time: {}"
                , appointmentRequest.getDateTime().toLocalDate(), appointmentRequest.getDateTime().toLocalTime());

        validateAppointmentExists(
                appointmentRepository.findAllByUserId(user.getId()),
                appointmentRequest.getDateTime().toLocalDate(),
                appointmentRequest.getDateTime().toLocalTime(),
                "You have already booked an appointment on this day and time."
        );

        dcLogger.info("User appointments validated");
    }

    private void validateUserSpecialistAppointment(Long userId, Long specialistId, LocalDate date) {
        dcLogger.debug("Validating user specialist appointment - User ID: {}, Specialist ID: {}, Date: {}", userId, specialistId, date);
        List<Appointment> appointments = appointmentRepository.findAllByUserIdAndSpecialistId(userId, specialistId);

        boolean exists = appointments.stream().anyMatch(a -> a
                .getDateTime()
                .toLocalDate()
                .equals(date) &&
                a.getAppointmentStatus()
                .getAppointmentStatus()
                .equals("Upcoming"));

        if (exists) {
            dcLogger.warn("User already has an appointment with the current doctor on the specified date.");
            throw new AppointmentDeniedException(ApiError.builder()
                    .path(request.getRequestURI())
                    .error("The user has already booked an appointment for the current doctor on that date.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

    }

    void validateAppointmentExists(List<Appointment> appointments, LocalDate date, LocalTime time, String errorMessage) {
        dcLogger.info("Validating appointment existence for date: {} and time: {}", date, time);

        boolean exists = appointments.stream()
                .anyMatch(a -> a.getDateTime().toLocalDate().equals(date)
                        && a.getDateTime().toLocalTime().equals(time)
                        && a.getAppointmentStatus().getAppointmentStatus().equals("Upcoming"));

        if (exists) {
            dcLogger.warn("Appointment denied: {}", errorMessage);
            throw new AppointmentDeniedException(ApiError.builder()
                    .path(request.getRequestURI())
                    .error(errorMessage)
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        dcLogger.info("Appointment validation successful");
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        if (!isWeekday(date.getDayOfWeek()) || !isWithinRange(time, LocalTime.of(8, 59), LocalTime.of(16, 1))) {
            dcLogger.warn("Methods(isWeekDay, isWithinRange): Date:{} Time:{}", date, time);
            throw new AppointmentDeniedException(ApiError.builder()
                    .path(request.getRequestURI())
                    .error("Selected time is out of the working day of the current specialist.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        if (isPastDateTime(date, time)) {
            dcLogger.warn("Method(isPastDateTime): Date:{} Time:{}", date, time);
            throw new AppointmentDeniedException(ApiError.builder()
                    .path(request.getRequestURI())
                    .error("The user can't schedule an appointment in the past.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        if (isDateNotInRange(date)) {
            dcLogger.warn("Method(isDateNotInRange): Date{}", date);
            throw new AppointmentDeniedException(ApiError.builder()
                    .path(request.getRequestURI())
                    .error("The user can't book an appointment on date later than one month from tomorrow.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        dcLogger.info("Date and time are valid");
    }

    private boolean isDateNotInRange(LocalDate date) {
        return date.isAfter(LocalDate.now().plusDays(32));
    }

    private boolean isPastDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).isBefore(LocalDateTime.now());
    }


    private boolean isWithinRange(LocalTime time, LocalTime startTime, LocalTime endTime) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    private boolean isWeekday(DayOfWeek day) {
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }
}
