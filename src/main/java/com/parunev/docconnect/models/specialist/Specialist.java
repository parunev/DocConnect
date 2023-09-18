package com.parunev.docconnect.models.specialist;

import com.parunev.docconnect.models.City;
import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.JwtToken;
import com.parunev.docconnect.models.Specialty;
import com.parunev.docconnect.models.commons.BaseEntity;
import com.parunev.docconnect.models.enums.Gender;
import com.parunev.docconnect.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The `Specialist` entity class represents a specialist within the application. It contains
 * detailed information about the specialist, including their email, password, name,
 * gender, role, city, country, address, specialty and others.

 * Pretty similar to the User entity, but with some differences.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SPECIALISTS")
@AttributeOverride(name = "id", column = @Column(name = "SPECIALIST_ID"))
public class Specialist extends BaseEntity implements UserDetails {

    /**
     * The first name of the specialist.
     */
    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    /**
     * The last name of the specialist.
     */
    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    /**
     * The phone number of the specialist.
     */
    @Column(name = "PHONE_NUMBER", length = 50)
    private String phoneNumber;

    /**
     * The email address of the specialist.
     */
    @Column(name = "EMAIL", length = 100)
    private String email;

    /**
     * The password of the specialist.
     */
    @Column(name = "PASSWORD", length = 100)
    private String password;

    /**
     * The summary of the specialist.
     */
    @Column(name = "SUMMARY", columnDefinition = "TEXT")
    private String summary;

    /**
     * The experience years of the specialist.
     */
    @Column(name = "EXPERIENCE_YEARS", nullable = false)
    private int experienceYears;

    /**
     * The gender of the specialist.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    /**
     * The role or authority of the specialist.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    /**
     * The JWT tokens associated with the specialist.
     */
    @OneToMany(mappedBy = "specialist")
    private List<JwtToken> tokens;

    /**
     * The city associated with the specialist.
     */
    @ManyToOne
    @JoinColumn(name = "CITY_ID",nullable = false)
    private City city;

    /**
     * The country associated with the specialist.
     */
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID",nullable = false)
    private Country country;

    /**
     * The specialty associated with the specialist.
     */
    @ManyToOne
    @JoinColumn(name = "SPECIALITY_ID",nullable = false)
    private Specialty specialty;

    /**
     * The addresses associated with the specialist.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "SPECIALISTS_ADDRESSES_MAPPING",
            joinColumns = @JoinColumn(name = "SPECIALIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "SPECIALIST_ADDR_ID")
    )
    private List<SpecialistAddress> addresses;

    /**
     * The image URL of the specialist.
     */
    @Column(name = "SPECIALIST_IMG_URL")
    private String imageUrl;

    /**
     * Indicates whether the specialist's account is enabled.
     */
    @Column(name = "IS_ENABLED", columnDefinition = "boolean default false")
    private boolean isEnabled;

    /**
     * Indicates whether the specialist has enabled the MFA authentication.
     */
    @Column(name = "MFA_ENABLED", columnDefinition = "boolean default false")
    private boolean mfaEnabled;

    /**
     * The MFA authentication secret key.
     */
    @Column(name = "MFA_SECRET")
    private String mfaSecret;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
