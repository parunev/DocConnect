package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "PASSWORD_RESET_TOKENS")
@AttributeOverride(name = "id", column = @Column(name = "PASSWORD_RESET_TOKEN_ID"))
public class PasswordToken extends BaseEntity {

    @Column(name = "PASSWORD_RESET_TOKEN", nullable = false)
    private String token;

    @Column(name = "USED_AT")
    private LocalDateTime usedAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;
}
