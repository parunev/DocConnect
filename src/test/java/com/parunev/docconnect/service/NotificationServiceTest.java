package com.parunev.docconnect.service;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.Specialty;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.UserProfile;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.models.specialist.SpecialistAddress;
import com.parunev.docconnect.repositories.AppointmentRepository;
import com.parunev.docconnect.repositories.UserProfileRepository;
import com.parunev.docconnect.services.NotificationService;
import com.parunev.docconnect.utils.email.EmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendAppointmentCanceledEmail() {
        User user = User.builder()
                .firstName("Test")
                .lastName("Test")
                .email("example@gmail.com")
                .build();

        UserProfile userProfile = new UserProfile();
        userProfile.setCanceledNotification(true);

        Specialty specialty = Specialty.builder()
                .specialtyName("Test naem")
                .imageUrl("https://image.url")
                .build();

        Specialist specialist = Specialist.builder()
                .firstName("Test")
                .lastName("Test")
                .specialty(specialty)
                .build();

        List<SpecialistAddress> addresses = Arrays.asList(
                new SpecialistAddress("123 Main St"),
                new SpecialistAddress("456 Elm St"));
        specialist.setAddresses(addresses);

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setSpecialist(specialist);
        appointment.setDateTime(LocalDateTime.now());

        when(userProfileRepository.findByUserId(user.getId())).thenReturn(userProfile);
        notificationService.sendAppointmentCanceledEmail(appointment);

        verify(emailSender).send(eq(user.getEmail()), anyString(), anyString());
    }
}
