package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import com.parunev.docconnect.models.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

/**
 * The `JwtToken` entity class represents a JSON Web Token (JWT) within the
 * application. It contains information about the token, including its type,
 * expiration status, revocation status, and the associated user.
 * <p>
 * Entity Attributes:
 * - `token`: A string representing the JWT token. It is limited to 1000
 *   characters and is non-nullable.
 * - `type`: An enumerated value indicating the type of token, such as "Bearer,"
 *   "Refresh," etc.
 * - `isExpired`: A boolean value indicating whether the token has expired.
 * - `isRevoked`: A boolean value indicating whether the token has been revoked.
 * - `user`: A many-to-one relationship with the `User` entity, representing the
 *   user associated with the token.
 * <p>
 * Entity Relationships:
 * - Many JWT tokens can belong to one user, established through the `user`
 *   attribute.
 * <p>
 * Usage:
 * The `JwtToken` entity is typically used to manage and track JWT tokens used
 * for authentication and authorization purposes. You can associate tokens with
 * users and check their expiration and revocation statuses.
 * <p>
 * Example:
 * ```java
 * // Create a new JWT token and associate it with a user
 * JwtToken jwtToken = JwtToken.builder()
 *         .token("some.jwt.token")
 *         .type(TokenType.BEARER)
 *         .isExpired(false)
 *         .isRevoked(false)
 *         .user(someUser) // Assign an existing User entity
 *         .build();
 * jwtTokenRepository.save(jwtToken);
 * <p>
 * // Check if a JWT token is expired or revoked
 * if (jwtToken.isExpired() || jwtToken.isRevoked()) {
 *     // Perform actions for expired or revoked tokens.
 * }
 * ```
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.JoinColumn
 * @see com.parunev.docconnect.models.commons.BaseEntity
 * @see com.parunev.docconnect.models.enums.TokenType
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "JWT_TOKENS")
@AttributeOverride(name = "id", column = @Column(name = "JWT_TOKEN_ID"))
public class JwtToken extends BaseEntity {

    /**
     * The JWT token.
     */
    @Column(name = "TOKEN", nullable = false, length = 1000)
    private String token;

    /**
     * The type of the JWT token, as an enumerated value.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TOKEN_TYPE", nullable = false)
    private TokenType type;

    /**
     * Indicates whether the token has expired.
     */
    @Column(name = "IS_EXPIRED", nullable = false)
    private boolean isExpired;

    /**
     * Indicates whether the token has been revoked.
     */
    @Column(name = "IS_REVOKED", nullable = false)
    private boolean isRevoked;

    /**
     * The user associated with the JWT token, established through a many-to-one
     * relationship.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

}
