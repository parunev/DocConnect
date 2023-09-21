package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.*;
import com.parunev.docconnect.models.enums.TokenType;
import com.parunev.docconnect.models.payloads.user.login.ForgotPasswordResponse;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.models.payloads.user.login.VerificationResponse;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.models.specialist.SpecialistAddress;
import com.parunev.docconnect.repositories.*;
import com.parunev.docconnect.security.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthHelpersTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordTokenRepository passwordTokenRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private SpecialistAddressRepository specialistAddressRepository;

    @Mock
    private JwtTokenRepository jwtTokenRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthHelpers authHelpers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateEmailAndProceed_UniqueEmail_ShouldReturnEmail() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(specialistRepository.existsByEmail(email)).thenReturn(false);

        String result = authHelpers.validateEmailAndProceed(email);

        assertEquals(email, result);
    }

    @Test
    void validateEmailAndProceed_DuplicateEmail_ShouldThrowException() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(specialistRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(EmailAlreadyExistsAuthenticationException.class, () ->
                authHelpers.validateEmailAndProceed(email));
    }

    @Test
    void validateToken_ValidToken_ShouldReturnTrue() {
        ConfirmationToken token = new ConfirmationToken();
        token.setExpiresAt(LocalDateTime.now().plusHours(1));

        boolean result = authHelpers.validateToken(token);

        assertTrue(result);
    }

    @Test
    void validateToken_ExpiredToken_ShouldThrowException() {
        ConfirmationToken token = new ConfirmationToken();
        token.setExpiresAt(LocalDateTime.now().minusHours(1));

        assertThrows(InvalidEmailTokenException.class, () -> authHelpers.validateToken(token));
    }

    @Test
    void validateToken_AlreadyConfirmedToken_ShouldThrowException() {
        ConfirmationToken token = new ConfirmationToken();
        token.setConfirmedAt(LocalDateTime.now());

        assertThrows(InvalidEmailTokenException.class, () -> authHelpers.validateToken(token));
    }

    @ParameterizedTest
    @CsvSource({
            "'', ''",
            "a, A",
            "apple, Apple",
            "hello world, Hello world",
            "HeLLo WoRLD, Hello world"
    })
    void capitalizeFirstLetter_ShouldReturnCapitalizedString(String input, String expected) {
        String result = authHelpers.capitalizeFirstLetter(input);
        assertEquals(expected, result);
    }

    @Test
    void revokeUserTokens_ShouldNotRevokeTokensIfNoneFound() {
        long userId = 1L;
        when(jwtTokenRepository.findAllValidTokensByUser(userId)).thenReturn(List.of());

        authHelpers.revokeUserTokens(new User());

        verify(jwtTokenRepository, times(0)).save(new JwtToken());
    }

    @Test
    void revokeSpecialistTokens_ShouldNotRevokeTokensIfNoneFound() {
        long specialistId = 1L;
        when(jwtTokenRepository.findAllValidTokensByUser(specialistId)).thenReturn(List.of());

        authHelpers.revokeUserTokens(new Specialist());

        verify(jwtTokenRepository, times(0)).save(new JwtToken());
    }

    @Test
    void testSaveJwtTokenToRepositoryForUser() {
        User user = new User(); // Create a User object
        String accessToken = "your_access_token";

        authHelpers.saveJwtTokenToRepository(user, accessToken);

        verify(jwtTokenRepository).save(argThat(jwtToken -> jwtToken != null
                && jwtToken.getToken().equals(accessToken)
                && jwtToken.getType() == TokenType.BEARER
                && !jwtToken.isExpired()
                && !jwtToken.isRevoked()
                && jwtToken.getUser() != null
                && jwtToken.getUser().equals(user)));
    }

    @Test
    void testSaveJwtTokenToRepositoryForSpecialist() {
        Specialist specialist = new Specialist(); // Create a User object
        String accessToken = "your_access_token";

        authHelpers.saveJwtTokenToRepository(specialist, accessToken);

        verify(jwtTokenRepository).save(argThat(jwtToken -> jwtToken != null
                && jwtToken.getToken().equals(accessToken)
                && jwtToken.getType() == TokenType.BEARER
                && !jwtToken.isExpired()
                && !jwtToken.isRevoked()
                && jwtToken.getSpecialist() != null
                && jwtToken.getSpecialist().equals(specialist)));
    }

    @Test
    void testReturnUserIfPresentWhenUserExists() {
        String email = "test@example.com";
        User expectedUser = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User returnedUser = authHelpers.returnUserIfPresent(email);

        assertNotNull(returnedUser);
        assertEquals(expectedUser, returnedUser);

        verify(userRepository).findByEmail(email);
    }

    @Test
    void testReturnUserIfPresentWhenUserDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class, () -> authHelpers.returnUserIfPresent(email));

        verify(userRepository).findByEmail(email);
    }

    @Test
    void testCreateLoginResponseForUser() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        User user = new User();
        String message = "Login successful";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        String secretImageUri = "image-uri";
        boolean mfaEnabled = true;

        LoginResponse response = authHelpers.createLoginResponse(user, message, accessToken, refreshToken, secretImageUri, mfaEnabled);

        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(user.getEmail(), response.getEmailAddress());
        assertEquals(secretImageUri, response.getSecretImageUri());
        assertEquals(mfaEnabled, response.isMfaEnabled());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testCreateLoginResponseForSpecialist() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        Specialist specialist = new Specialist();
        String message = "Login successful";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        String secretImageUri = "image-uri";
        boolean mfaEnabled = true;

        LoginResponse response = authHelpers.createLoginResponse(specialist, message, accessToken, refreshToken, secretImageUri, mfaEnabled);

        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(specialist.getEmail(), response.getEmailAddress());
        assertEquals(secretImageUri, response.getSecretImageUri());
        assertEquals(mfaEnabled, response.isMfaEnabled());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testCreateRegistrationResponse() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        String message = "Registration successful";
        String emailAddress = "user@example.com";
        HttpStatus status = HttpStatus.OK;

        RegistrationResponse response = authHelpers.createRegistrationResponse(message, emailAddress, status);

        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(emailAddress, response.getEmailAddress());
        assertNotNull(response.getTimestamp());
        assertEquals(status, response.getStatus());
    }

    @Test
    void testValidateCityAndProceedWhenCityExists() {
        Long cityId = 1L;
        City expectedCity = new City();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(expectedCity));

        City city = authHelpers.validateCityAndProceed(cityId);

        assertNotNull(city);
        assertSame(expectedCity, city);
    }

    @Test
    void testValidateCityAndProceedWhenCityDoesNotExist() {
        Long cityId = 2L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        assertThrows(CityServiceException.class, () -> authHelpers.validateCityAndProceed(cityId));
    }

    @Test
    void testValidateCountryAndProceedWhenCountryExists() {
        Long countryId = 1L;

        Country expectedCountry = new Country();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(expectedCountry));

        Country country = authHelpers.validateCountryAndProceed(countryId);

        assertNotNull(country);
        assertSame(expectedCountry, country);
    }

    @Test
    void testValidateCountryAndProceedWhenCountryDoesNotExist() {
        Long countryId = 2L;

        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        assertThrows(CountryServiceException.class, () -> authHelpers.validateCountryAndProceed(countryId));
    }

    @Test
    void testValidateSpecialtyAndProceedWhenSpecialtyExists() {
        Long specialtyId = 1L;

        Specialty expectedSpecialty = new Specialty();
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(expectedSpecialty));

        Specialty specialty = authHelpers.validateSpecialtyAndProceed(specialtyId);

        assertNotNull(specialty);
        assertSame(expectedSpecialty, specialty);
    }

    @Test
    void testValidateSpecialtyAndProceedWhenSpecialtyDoesNotExist() {
        Long specialtyId = 2L;

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        assertThrows(SpecialtyServiceException.class, () -> authHelpers.validateSpecialtyAndProceed(specialtyId));
    }

    @Test
    void testExtractAddresses() {
        List<String> addresses = Arrays.asList("Address1", "Address2");

        when(specialistAddressRepository.save(any(SpecialistAddress.class)))
                .thenAnswer(invocation -> invocation.<SpecialistAddress>getArgument(0));

        List<SpecialistAddress> extractedAddresses = authHelpers.extractAddresses(addresses);

        assertNotNull(extractedAddresses);
        assertEquals(2, extractedAddresses.size());

        verify(specialistAddressRepository, times(2)).save(any(SpecialistAddress.class));
    }

    @Test
    void testReturnSpecialistIfPresent() {
        String emailAddress = "test@example.com";

        Specialist specialist = new Specialist();
        specialist.setEmail(emailAddress);

        when(specialistRepository.findByEmail(emailAddress)).thenReturn(Optional.of(specialist));

        Specialist result = authHelpers.returnSpecialistIfPresent(emailAddress);

        assertNotNull(result);
        assertEquals(emailAddress, result.getEmail());

        verify(specialistRepository).findByEmail(emailAddress);
    }

    @Test
    void testReturnSpecialistIfNotPresent() {
        String emailAddress = "nonexistent@example.com";

        when(specialistRepository.findByEmail(emailAddress)).thenReturn(Optional.empty());

        InvalidLoginException exception = assertThrows(InvalidLoginException.class, () -> {
            authHelpers.returnSpecialistIfPresent(emailAddress);
        });

        assertEquals("Specialist not found.", exception.getAuthenticationError().getError());
        assertEquals(HttpStatus.NOT_FOUND, exception.getAuthenticationError().getStatus());

        verify(specialistRepository).findByEmail(emailAddress);
    }

    @Test
    void testCreateVerificationResponse() {
        String message = "Verification successful";
        String emailAddress = "test@example.com";
        HttpStatus status = HttpStatus.OK;

        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        VerificationResponse response = authHelpers.createVerificationResponse(message, emailAddress, status);

        assertEquals("/api/v1/auth", response.getPath());
        assertEquals(message, response.getMessage());
        assertEquals(emailAddress, response.getEmailAddress());
        assertEquals(status, response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testCreateForgotPasswordResponse() {
        String message = "Password reset email sent";
        String emailAddress = "test@example.com";
        HttpStatus status = HttpStatus.OK;

        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        ForgotPasswordResponse response = authHelpers.createForgotPasswordResponse(message, emailAddress, status);

        assertEquals("/api/v1/auth", response.getPath());
        assertEquals(message, response.getMessage());
        assertEquals(emailAddress, response.getEmailAddress());
        assertEquals(status, response.getStatus());

        assertNotNull(response.getTimestamp());
    }

    @Test
    void testExtractFullName_ValidFullName() {
        String fullName = "John Doe";

        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        String[] nameParts = authHelpers.extractFullName(fullName);

        assertNotNull(nameParts);
        assertEquals(2, nameParts.length);
        assertEquals("John", nameParts[0]);
        assertEquals("Doe", nameParts[1]);
    }

    @Test
    void testExtractFullName_FirstNameOnly() {
        String fullName = "John";

        when(request.getRequestURI()).thenReturn("/api/v1/auth");

        try {
            authHelpers.extractFullName(fullName);
            fail("Expected FullNameNotFoundException");
        } catch (FullNameNotFoundException e) {
            assertEquals("/api/v1/auth", e.getError().getPath());
            assertEquals("Only first name was found through your provider!", e.getError().getError());
            assertNotNull(e.getError().getTimestamp());
            assertEquals(HttpStatus.NOT_FOUND, e.getError().getStatus());
        }
    }

}
