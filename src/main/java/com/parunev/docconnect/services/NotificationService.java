package com.parunev.docconnect.services;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.UserProfile;
import com.parunev.docconnect.models.enums.Status;
import com.parunev.docconnect.repositories.AppointmentRepository;
import com.parunev.docconnect.repositories.UserProfileRepository;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.parunev.docconnect.utils.email.Patterns.*;

/**
 * The {@code NotificationService} class is responsible for handling email notifications related to appointments
 * in the DocConnect application. It provides methods to send email notifications for appointment cancellations,
 * upcoming appointment reminders, and feedback requests to users.
 *
 * <p>This service class interacts with the {@link com.parunev.docconnect.repositories.UserProfileRepository} and
 * {@link com.parunev.docconnect.repositories.AppointmentRepository} repositories to access user profiles and appointment
 * information required for sending notifications.
 *
 * <p>The class includes scheduled methods that run at specific times to automatically send appointment reminders
 * and feedback requests to users based on their preferences and appointment schedules.
 *
 * <p>Overall, the {@code NotificationService} plays a crucial role in enhancing the user experience by keeping
 * users informed about their appointments and soliciting feedback on their experiences.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserProfileRepository userProfileRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmailSender emailSender;
    private final DCLogger dcLogger = new DCLogger(NotificationService.class);

    /**
     * Sends an email notification to the user when their appointment is canceled.
     *
     * @param appointment The canceled appointment.
     */
    public void sendAppointmentCanceledEmail(Appointment appointment) {
        UserProfile userProfile = userProfileRepository.findByUserId(appointment.getUser().getId());
        if (userProfile.isCanceledNotification()) {
            emailSender.send(appointment.getUser().getEmail(),
                    buildAppointmentCanceledEmail(
                            appointment.getUser().getName(),
                            appointment.getSpecialist().getFirstName()
                                    + " "
                                    + appointment.getSpecialist().getLastName(),
                            appointment.getSpecialist().getSpecialty().getSpecialtyName(),
                            appointment.getSpecialist().getAddresses().get(0).getDocAddress(),
                            appointment),
                    "DocConnect: Appointment Canceled");
            dcLogger.info("A cancel appointment email was sent to: " + appointment.getUser().getEmail());
        }
    }

    /**
     * Sends email reminders for upcoming appointments to users.
     * This method is scheduled to run periodically.
     */
    @Scheduled(cron = "0 0 9-16 * * ?")
    public void sendAppointmentRemindingEmail() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        appointmentRepository.findAllByAppointmentStatus(Status.STATUS_UPCOMING).stream()
                .filter(
                        appointment ->
                                LocalDateTime.of(appointment.getDateTime().toLocalDate(), appointment.getDateTime().toLocalTime())
                                        .format(formatter)
                                        .equals(LocalDateTime.now().withMinute(0).plusHours(24).format(formatter)))
                .forEach(
                        appointment -> {
                            UserProfile userProfile = userProfileRepository.findByUserId(appointment.getUser().getId());
                            if (userProfile.isUpcomingNotification()) {
                                emailSender.send(
                                        appointment.getUser().getEmail(),
                                        buildAppointmentReminderEmail(
                                                appointment.getUser().getName(),
                                                appointment.getSpecialist().getFirstName()
                                                        + " "
                                                        + appointment.getSpecialist().getLastName(),
                                                appointment.getSpecialist().getSpecialty().getSpecialtyName(),
                                                appointment.getSpecialist().getAddresses().get(0).getDocAddress(),
                                                appointment),
                                        "DocConnect: Appointment Reminder");
                                dcLogger.info("A reminding email was sent to: " + appointment.getUser().getEmail());
                            }
                        });
    }

    /**
     * Sends email reminders to users for providing feedback on completed appointments.
     * This method is scheduled to run periodically.
     */
    @Scheduled(cron = "0 0 11-18 * * ?")
    public void sendFeedbackEmail() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        appointmentRepository.findAllByAppointmentStatus(Status.STATUS_COMPLETED).stream()
                .filter(
                        appointment ->
                                LocalDateTime.of(appointment.getDateTime().toLocalDate(), appointment.getDateTime().toLocalTime())
                                        .format(formatter)
                                        .equals(LocalDateTime.now().withMinute(0).minusHours(2).format(formatter)))
                .forEach(
                        appointment -> {
                            UserProfile userProfile = userProfileRepository.findByUserId(appointment.getUser().getId());
                            if (userProfile.isFeedbackNotification()) {
                                emailSender.send(
                                        appointment.getUser().getEmail(),
                                        buildFeedbackEmail(
                                                appointment.getUser().getName(),
                                                appointment.getSpecialist().getFirstName()
                                                        + " "
                                                        + appointment.getSpecialist().getLastName(),
                                                appointment.getSpecialist().getSpecialty().getSpecialtyName(),
                                                appointment.getSpecialist().getAddresses().get(0).getDocAddress(),
                                                appointment,
                                                appointment.getSpecialist().getId()),
                                        "DocConnect: Appointment Feedback");
                                dcLogger.info("A reminding email was sent to: " + appointment.getUser().getEmail());
                            }
                        });
    }
}
