package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import com.parunev.docconnect.models.specialist.Specialist;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The {@code ConfirmationToken} class represents a confirmation token used for email verification in a Spring-based application.
 * It extends the {@code BaseEntity} class and is used to associate a user with a confirmation token.
 *
 * <p>Each confirmation token has properties such as the confirmation timestamp, expiration timestamp, and the actual token string.
 * It also has a many-to-one relationship with the {@code User} entity, which associates a token with a specific user.
 *
 * @see BaseEntity
 * @see User
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "CONFIRMATION_TOKENS")
@AttributeOverride(name = "id", column = @Column(name = "CONFIRMATION_TOKEN_ID"))
public class ConfirmationToken extends BaseEntity {

    /** Timestamp when the confirmation was completed */
    @Column(name = "CONFIRMED_AT")
    private LocalDateTime confirmedAt;

    /** Timestamp when the token expires (must not be null) */
    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    /** The confirmation token string (must not be null) */
    @Column(name = "CONFIRMATION_TOKEN", nullable = false)
    private String token;

    /** The associated user (many-to-one relationship with User) */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    /** The associated specialist (many-to-one relationship with Specialist) */
    @ManyToOne
    @JoinColumn(name = "SPECIALIST_ID")
    private Specialist specialist;
}
