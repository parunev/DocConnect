package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The `PasswordToken` class represents a token used for password reset functionality.
 * It extends the `BaseEntity` class, inheriting basic entity properties such as ID and creation/update timestamps.
 * <p>
 * This entity stores information about password reset tokens, including the token string itself,
 * when it was used (if applicable), and when it expires. It is associated with a specific user
 * who requested the password reset.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "PASSWORD_RESET_TOKENS")
@AttributeOverride(name = "id", column = @Column(name = "PASSWORD_RESET_TOKEN_ID"))
public class PasswordToken extends BaseEntity {

    /**
     * The token string used for password reset.
     */
    @Column(name = "PASSWORD_RESET_TOKEN", nullable = false)
    private String token;

    /**
     * The timestamp indicating when the token was used for password reset.
     * This field may be null if the token hasn't been used yet.
     */
    @Column(name = "USED_AT")
    private LocalDateTime usedAt;

    /**
     * The timestamp indicating when the token expires.
     * Password reset tokens should be used before this timestamp to be considered valid.
     */
    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * The user associated with this password reset token.
     * This field should not be null, as every token must belong to a specific user.
     */
    @ManyToOne
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;
}
