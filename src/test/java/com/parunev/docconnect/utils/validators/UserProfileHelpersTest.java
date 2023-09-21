package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.City;
import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.AuthProvider;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.repositories.CityRepository;
import com.parunev.docconnect.repositories.CountryRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.CityServiceException;
import com.parunev.docconnect.security.exceptions.CountryServiceException;
import com.parunev.docconnect.security.exceptions.InvalidPasswordChangeException;
import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserProfileHelpersTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private UserProfileHelpers userProfileHelpers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUser_UserFound_ShouldReturnUser() {
        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email(userEmail)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        User user = User.builder()
                .email(userEmail)
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        User actualUser = userProfileHelpers.findUser();

        assertEquals(user, actualUser);
    }

    @Test
    void findUser_UserNotFound_ShouldThrowUserNotFoundException() {
        String userEmail = "user@example.com";
        UserDetails userDetails = User.builder()
                .email(userEmail)
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userProfileHelpers.findUser());
    }

    @Test
    void validateAndReturnCountry_CountryFound_ShouldReturnCountry() {
        Long countryId = 1L;
        Country expectedCountry = new Country();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(expectedCountry));

        Country actualCountry = userProfileHelpers.validateAndReturnCountry(countryId);

        assertEquals(expectedCountry, actualCountry);
    }

    @Test
    void validateAndReturnCountry_CountryNotFound_ShouldThrowCountryServiceException() {
        Long countryId = 2L;
        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        assertThrows(CountryServiceException.class, () -> userProfileHelpers.validateAndReturnCountry(countryId));
    }

    @Test
    void validateAndReturnCity_CityFound_ShouldReturnCity() {
        Long cityId = 1L;
        City expectedCity = new City();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(expectedCity));
        City actualCity = userProfileHelpers.validateAndReturnCity(cityId);

        assertEquals(expectedCity, actualCity);
    }

    @Test
    void validateAndReturnCity_CityNotFound_ShouldThrowCityServiceException() {
        Long cityId = 2L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        assertThrows(CityServiceException.class, () -> userProfileHelpers.validateAndReturnCity(cityId));
    }

    @Test
    void checkProvider_LocalProvider_ShouldReturnTrue() {
        AuthProvider provider = AuthProvider.LOCAL;

        boolean result = userProfileHelpers.checkProvider(provider);

        assertTrue(result);
    }

    @Test
    void checkProvider_NonLocalProvider_ShouldThrowInvalidPasswordChangeException() {
        AuthProvider provider = AuthProvider.GOOGLE;

        InvalidPasswordChangeException exception = assertThrows(
                InvalidPasswordChangeException.class,
                () -> userProfileHelpers.checkProvider(provider)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getAuthenticationError().getStatus());
    }


    @Test
    void checkPasswordMatching_NonMatchingPasswords_ShouldThrowInvalidPasswordChangeException() {
        String oldPassword = "oldPassword";
        String hashedPassword = passwordEncoder.encode("differentPassword");

        InvalidPasswordChangeException exception = assertThrows(
                InvalidPasswordChangeException.class,
                () -> userProfileHelpers.checkPasswordMatching(oldPassword, hashedPassword)
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getAuthenticationError().getStatus());
    }

    @Test
    void checkNewPassword_MatchingPasswords_ShouldReturnTrue() {
        String newPassword = "newPassword";

        boolean result = userProfileHelpers.checkNewPassword(newPassword, newPassword);

        assertTrue(result);
    }

    @Test
    void checkNewPassword_NonMatchingPasswords_ShouldThrowInvalidPasswordChangeException() {
        String newPassword = "newPassword";
        String confirmNewPassword = "differentPassword";

        InvalidPasswordChangeException exception = assertThrows(
                InvalidPasswordChangeException.class,
                () -> userProfileHelpers.checkNewPassword(newPassword, confirmNewPassword)
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getAuthenticationError().getStatus());
    }
}
