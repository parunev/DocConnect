package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * The {@code UserProfile} class represents additional user profile information and preferences.
 *
 * <p>The user profile includes details such as weight, height, blood pressure, blood sugar level,
 * and notification preferences for upcoming, canceled, and feedback notifications.
 *
 * @see BaseEntity
 * @see User
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "USER_PROFILES")
@AttributeOverride(name = "id", column = @Column(name = "PROFILE_ID"))
public class UserProfile extends BaseEntity {

    /**
     * The weight of the user in kilograms.
     */
    @Column(name = "WEIGHT_IN_KG")
    private Integer weight;

    /**
     * The height of the user in centimeters.
     */
    @Column(name = "HEIGHT_IN_CM")
    private Integer height;

    /**
     * The user's blood pressure.
     */
    @Column(name = "BLOOD_PRESSURE")
    private String bloodPressure;

    /**
     * The user's blood sugar level.
     */
    @Column(name = "BLOOD_SUGAR_LEVEL")
    private Double bloodSugarLevel;

    /**
     * A boolean indicating whether the user wants to receive upcoming notifications (default is true).
     */
    @Column(name = "UPCOMMING_NOTIFICATION", columnDefinition = "boolean default true")
    private boolean upcomingNotification;

    /**
     * A boolean indicating whether the user wants to receive canceled notifications (default is true).
     */
    @Column(name = "CANCELED_NOTIFICATION", columnDefinition = "boolean default true")
    private boolean canceledNotification;

    /**
     * A boolean indicating whether the user wants to receive feedback notifications (default is true).
     */
    @Column(name = "FEEDBACK_NOTIFICATION", columnDefinition = "boolean default true")
    private boolean feedbackNotification;

    /**
     * The user associated with this profile.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
