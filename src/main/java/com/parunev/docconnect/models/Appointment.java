package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import com.parunev.docconnect.models.enums.Status;
import com.parunev.docconnect.models.specialist.Specialist;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The {@code Appointment} class represents a scheduled appointment in the DocConnect application.
 * Each appointment is associated with a date and time, a status, and involves a specialist and a user.
 *
 * <p>This class extends the {@link com.parunev.docconnect.models.commons.BaseEntity} class, inheriting
 * common properties such as the unique identifier for entities.
 *
 * <p>The main attributes of an appointment include:
 * <ul>
 *     <li>{@code dateTime} - The date and time of the appointment.</li>
 *     <li>{@code appointmentStatus} - The current status of the appointment (e.g., upcoming, completed, or canceled).</li>
 *     <li>{@code specialist} - The specialist (doctor) associated with the appointment.</li>
 *     <li>{@code user} - The user (patient) who scheduled the appointment.</li>
 * </ul>
 *
 * <p>This class uses the JPA (Java Persistence API) annotations to map it to a database table named "APPOINTMENTS"
 * with an "APPOINTMENT_ID" column as the primary key.
 *
 * <p>The {@code Appointment} class is an integral part of managing appointments within the DocConnect application.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "APPOINTMENTS")
@AttributeOverride(name = "id", column = @Column(name = "APPOINTMENT_ID"))
public class Appointment extends BaseEntity {

    /**
     * The date and time of the appointment.
     */
    @Column(name = "APPOINTMENT_DATETIME")
    private LocalDateTime dateTime;

    /**
     * The current status of the appointment (e.g., upcoming, completed, or canceled).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "APPOINTMENT_STATUS", nullable = false)
    private Status appointmentStatus;

    /**
     * The specialist (doctor) associated with the appointment.
     */
    @ManyToOne
    @JoinColumn(name = "SPECIALIST_ID")
    private Specialist specialist;

    /**
     * The user (patient) who scheduled the appointment.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
