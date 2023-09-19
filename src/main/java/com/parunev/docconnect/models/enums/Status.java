package com.parunev.docconnect.models.enums;

import lombok.Getter;
/**
 * The {@code Status} enum represents the possible statuses of an appointment in the DocConnect application.
 * Each status corresponds to a different stage in the appointment lifecycle.
 *
 * <p>The available status values are:
 * <ul>
 *     <li>{@link #STATUS_UPCOMING} - Indicates an upcoming appointment.</li>
 *     <li>{@link #STATUS_COMPLETED} - Indicates a completed appointment.</li>
 *     <li>{@link #STATUS_CANCELED} - Indicates a canceled appointment.</li>
 * </ul>
 *
 * <p>Each status is associated with a descriptive string that provides a human-readable representation
 * of the status.
 *
 */
@Getter
public enum Status {

    /**
     * Represents an upcoming appointment that is scheduled for a future date.
     */
    STATUS_UPCOMING("Upcoming"),

    /**
     * Represents an appointment that has been successfully completed.
     */
    STATUS_COMPLETED("Completed"),

    /**
     * Represents an appointment that has been canceled by either the patient or the provider.
     */
    STATUS_CANCELED("Canceled");

    /**
     * The human-readable description of the appointment status.
     */
    private final String appointmentStatus;

    /**
     * Constructs a Status enum value with the given appointment status.
     *
     * @param appointmentStatus The descriptive string representing the appointment status.
     */
    Status(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
