package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import com.parunev.docconnect.models.enums.AuthProvider;
import com.parunev.docconnect.models.enums.Gender;
import com.parunev.docconnect.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The `User` entity class represents a user within the application. It contains
 * detailed information about the user, including their email, password, name,
 * gender, role, and authentication provider.
 * <p>
 * Entity Attributes:
 * - `email`: A string representing the email address of the user. It is limited
 *   to 100 characters and is non-nullable.
 * - `password`: A string representing the user's password.
 * - `firstName`: The user's first name, limited to 100 characters and non-nullable.
 * - `lastName`: The user's last name, limited to 100 characters and non-nullable.
 * - `gender`: An enumerated value representing the gender of the user.
 * - `country`: A many-to-one relationship with the `Country` entity, indicating
 *   the country associated with the user.
 * - `city`: A many-to-one relationship with the `City` entity, indicating the
 *   city associated with the user.
 * - `role`: An enumerated value representing the role or authority of the user.
 * - `tokens`: A one-to-many relationship with the `JwtToken` entity, representing
 *   the JWT tokens associated with the user.
 * - `provider`: An enumerated value representing the authentication provider
 *   used by the user (e.g., local or Google).
 * - `providerId`: A string representing the provider-specific identifier for
 *   the user.
 * - `imageUrl`: A string representing the URL of the user's profile image.
 * - `isEnabled`: A boolean indicating whether the user's account is enabled.
 * <p>
 * Entity Relationships:
 * - Many users can belong to one country, established through the `country`
 *   attribute.
 * - Many users can belong to one city, established through the `city` attribute.
 * - One user can have many JWT tokens, established through the `tokens`
 *   attribute.
 * <p>
 * Implemented Interfaces:
 * - `OAuth2User`: The entity implements this interface to support OAuth2-based
 *   authentication and authorization.
 * - `UserDetails`: The entity implements this interface to provide user
 *   details for authentication purposes.
 * <p>
 * Usage:
 * The `User` entity is used to store detailed user information and manage
 * user authentication and authorization. It supports OAuth2 authentication and
 * Spring Security's `UserDetails` interface for user authentication.
 * <p>
 * Example:
 * ```java
 * // Create a new user entity and set its attributes
 * User user = User.builder()
 *         .email("user@example.com")
 *         .password("password")
 *         .firstName("John")
 *         .lastName("Doe")
 *         .gender(Gender.MALE)
 *         .country(someCountry) // Assign a Country entity
 *         .city(someCity) // Assign a City entity
 *         .role(Role.ROLE_USER)
 *         .provider(AuthProvider.LOCAL)
 *         .providerId("local_user_id")
 *         .imageUrl("example.com/avatar.jpg")
 *         .isEnabled(true)
 *         .build();
 * userRepository.save(user);
 * <p>
 * // Retrieve user details for authentication
 * UserDetails userDetails = userService.loadUserByUsername("user@example.com");
 * ```
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.OneToMany
 * @see jakarta.persistence.JoinColumn
 * @see com.parunev.docconnect.models.commons.BaseEntity
 * @see com.parunev.docconnect.models.enums.Gender
 * @see com.parunev.docconnect.models.enums.Role
 * @see com.parunev.docconnect.models.enums.AuthProvider
 * @see com.parunev.docconnect.models.JwtToken
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see org.springframework.security.oauth2.core.user.OAuth2User
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "USERS")
@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
public class User extends BaseEntity implements OAuth2User, UserDetails {

    /**
     * The email address of the user.
     */
    @Column(name = "USER_EMAIL", length = 100, nullable = false)
    private String email;

    /**
     * The user's password.
     */
    @Column(name = "USER_PASSWORD")
    private String password;

    /**
     * The user's first name.
     */
    @Column(name = "FIRST_NAME", length = 100, nullable = false)
    private String firstName;

    /**
     * The user's last name.
     */
    @Column(name = "LAST_NAME", length = 100, nullable = false)
    private String lastName;

    /**
     * The gender of the user.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    /**
     * The country associated with the user.
     */
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    /**
     * The city associated with the user.
     */
    @ManyToOne
    @JoinColumn(name = "CITY_ID")
    private City city;


    /**
     * The role or authority of the user.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    /**
     * The JWT tokens associated with the user.
     */
    @OneToMany(mappedBy = "user")
    private List<JwtToken> tokens;

    /**
     * The authentication provider used by the user.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PROVIDER", nullable = false)
    private AuthProvider provider;

    /**
     * The provider-specific identifier for the user.
     */
    @Column(name = "PROVIDER_ID")
    private String providerId;

    /**
     * The URL of the user's profile image.
     */
    @Column(name = "IMAGE_URL")
    private String imageUrl;

    /**
     * Indicates whether the user's account is enabled.
     */
    @Column(name = "IS_ENABLED", columnDefinition = "boolean default false")
    private boolean isEnabled;

    /**
     * Indicates whether the user has enabled the MFA authentication.
     */
    @Column(name = "MFA_ENABLED", columnDefinition = "boolean default false")
    private boolean mfaEnabled;

    /**
     * The MFA authentication secret key.
     */
    @Column(name = "MFA_SECRET")
    private String mfaSecret;


    /**
     * Additional attributes associated with the user, typically used in OAuth2
     * authentication.
     */
    private transient Map<String, Object> attributes;

    /**
     * Returns the authorities (roles) assigned to the user for authentication.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getAuthority()));
    }

    /**
     * Returns the user's password for authentication.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the user's email address as the username for authentication.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Indicates whether the user's account has not expired (always true).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is not locked (always true).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials are not expired (always true).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns the user's full name.
     */
    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
