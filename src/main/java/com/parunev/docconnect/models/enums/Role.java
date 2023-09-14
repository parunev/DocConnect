package com.parunev.docconnect.models.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
/**
 * The `Role` enum represents the different roles or authorities that users can
 * have within the application. Each role has an associated authority string.
 * This enum also implements the `GrantedAuthority` interface, making it
 * compatible with Spring Security.
 * <p>
 * Enum Values:
 * - `ROLE_ADMIN`: Represents an administrative role.
 * - `ROLE_USER`: Represents a standard user role.
 * <p>
 * Usage:
 * You can use the `Role` enum to assign roles to users and check their
 * authorities in your application. For example:
 * <p>
 * ```java
 * // Assign an administrative role to a user * .setRoles(Collections.singleton(Role.ROLE_ADMIN));
 * <p>
 * // Check if a user has a specific role
 * if (user.getRoles().contains(Role.ROLE_USER)) {
 *     // Perform actions for users with the "ROLE_USER" authority.
 * }
 * ```
 *
 * @see lombok.Getter
 * @see org.springframework.security.core.GrantedAuthority
 */
@Getter
public enum Role implements GrantedAuthority {

    /**
     * Represents an administrative role.
     */
    ROLE_ADMIN("ROLE_ADMIN"),

    /**
     * Represents a standard user role.
     */
    ROLE_USER("ROLE_USER");

    /**
     * The authority string associated with the role.
     */
    private final String authority;

    /**
     * Constructs a `Role` enum with the specified authority string.
     *
     * @param authority The authority string associated with the role.
     */
    Role(String authority) {
        this.authority = authority;
    }

    /**
     * Returns the authority string associated with the role.
     *
     * @return The authority string.
     */
    @Override
    public String getAuthority() {
        return authority;
    }
}
