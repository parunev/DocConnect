package com.parunev.docconnect.services;

import com.parunev.docconnect.models.PasswordToken;
import com.parunev.docconnect.models.Rating;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.models.payloads.specialist.SpecialistAddressResponse;
import com.parunev.docconnect.models.payloads.specialist.SpecialistRegistrationRequest;
import com.parunev.docconnect.models.payloads.specialist.SpecialistResponse;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import com.parunev.docconnect.models.payloads.user.login.*;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.PasswordTokenRepository;
import com.parunev.docconnect.repositories.RatingRepository;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.security.exceptions.AlreadyEnabledException;
import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.exceptions.SpecialistNotFoundException;
import com.parunev.docconnect.security.jwt.JwtService;
import com.parunev.docconnect.security.mfa.Email2FAuthentication;
import com.parunev.docconnect.security.mfa.Google2FAuthentication;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.email.EmailSender;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.parunev.docconnect.services.AuthService.CONFIRMATION_LINK;
import static com.parunev.docconnect.utils.email.Patterns.buildConfirmationEmail;
import static com.parunev.docconnect.utils.email.Patterns.buildRegistrationSuccessEmail;

@Service
@Validated
@RequiredArgsConstructor
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final RatingRepository ratingRepository;
    private final AuthHelpers authHelpers;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final AuthenticationManager authenticationManager;
    private final DCLogger dcLogger = new DCLogger(SpecialistService.class);
    private final Google2FAuthentication google2FAuthentication;
    private final Email2FAuthentication email2FAuthentication;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    /**
     * Registers a new specialist.
     *
     * @param request The specialist registration request.
     * @return A response indicating the registration status.
     */
    public RegistrationResponse registerSpecialist(@Valid SpecialistRegistrationRequest request) {
        dcLogger.info("Registering specialist with email: " + request.getEmail());

        Specialist specialist = buildSpecialistDto(request);
        specialistRepository.save(specialist);
        dcLogger.info("Specialist with email: " + request.getEmail() + " was successfully registered");

        dcLogger.info("Specialist with email: " + request.getEmail() + " is created. Awaits admin confirmation");
        return authHelpers.createRegistrationResponse("Specialist account is created. Awaits admin confirmation",
                specialist.getEmail(),
                HttpStatus.CREATED);

    }

    /**
     * Builds a Specialist entity from a registration request.
     *
     * @param request The specialist registration request.
     * @return The constructed Specialist entity.
     */
    private Specialist buildSpecialistDto(SpecialistRegistrationRequest request) {
        return Specialist.builder()
                .firstName(authHelpers.capitalizeFirstLetter(request.getFirstName()))
                .lastName(authHelpers.capitalizeFirstLetter(request.getLastName()))
                .phoneNumber(request.getPhoneNumber())
                .email(authHelpers.validateEmailAndProceed(request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .summary(request.getSummary())
                .experienceYears(request.getExperienceYears())
                .gender(request.getGender())
                .city(authHelpers.validateCityAndProceed(request.getCityId()))
                .country(authHelpers.validateCountryAndProceed(request.getCountryId()))
                .specialty(authHelpers.validateSpecialtyAndProceed(request.getSpecialtyId()))
                .addresses(authHelpers.extractAddresses(request.getAddresses()))
                .imageUrl(request.getImageUrl())
                .role(Role.ROLE_SPECIALIST)
                .mfaEnabled(false)
                .mfaSecret(google2FAuthentication.generateNewSecret())
                .build();
    }

    /**
     * Logs in a specialist and generates JWT tokens.
     *
     * @param request The login request.
     * @return A response containing login information and JWT tokens.
     */
    public LoginResponse specialistLogin(@Valid LoginRequest request){
        dcLogger.info("Specialist with email: " + request.getEmailAddress() + " is trying to login");

        Specialist specialist = authHelpers.returnSpecialistIfPresent(request.getEmailAddress());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmailAddress(), request.getPassword()));

        // Check if the specialist has MFA enabled
        if (specialist.isMfaEnabled()){
            if (specialist.getMfaSecret() == null){
                specialist.setMfaSecret(google2FAuthentication.generateNewSecret());
                specialistRepository.save(specialist);
            }

            // Generate a response indicating MFA is enabled and send QR code for setup
            dcLogger.info("Specialist with email: " + request.getEmailAddress() + " has MFA enabled. Sending QR code to email");
            return authHelpers.createLoginResponse(specialist,
                    "Specialist provided correct credentials. Please enter the code from the QR code.",
                    null,
                    null,
                    google2FAuthentication.generateQrCodeImageUri(specialist.getMfaSecret())
                    ,true);
        }

        dcLogger.info("Specialist with email: " + request.getEmailAddress() + " has MFA disabled. Generating JWT tokens");
        String accessToken = jwtService.generateToken(specialist);
        String refreshToken = jwtService.generateRefreshToken(specialist);
        authHelpers.revokeUserTokens(specialist);
        authHelpers.saveJwtTokenToRepository(specialist, accessToken);

        return authHelpers.createLoginResponse(specialist,
                "Specialist provided correct credentials. Logged in successfully.",
                accessToken,
                refreshToken,
                null,
                false);
    }

    /**
     * Confirms the registration of a specialist based on their ID. ROLE_ADMIN is required.
     *
     * @param specialistId The unique ID of the specialist to confirm.
     * @return A response indicating the successful confirmation of the specialist's account.
     * @throws SpecialistNotFoundException If the specialist with the provided ID is not found.
     */
    @Transactional
    public VerificationResponse confirmSpecialistRegistration(Long specialistId){
        dcLogger.info("Confirming specialist registration");
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> {
                    dcLogger.warn("Specialist with id: {} was not found", specialistId);
                    throw new SpecialistNotFoundException(AuthenticationError.builder()
                            .path(authHelpers.getRequest().getRequestURI())
                            .error("Specialist with the provided id was not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });

        if (specialist.isEnabled()){
            throw new AlreadyEnabledException(AuthenticationError.builder()
                    .path(authHelpers.getRequest().getRequestURI())
                    .error("Specialist with the provided id is already enabled.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        specialistRepository.enableSpecialist(specialist.getEmail());

        emailSender.send(specialist.getEmail(), buildRegistrationSuccessEmail(specialist.getFirstName() + " " + specialist.getLastName()),
                "DocConnect: Your DocConnect account was confirmed");

        dcLogger.info("Specialist with email: {} was successfully enabled", specialist.getEmail());
        return authHelpers.createVerificationResponse("Specialist account was successfully enabled",
                specialist.getEmail(),
                HttpStatus.OK);
    }

    /**
     * Verifies the login of a specialist using authentication (MFA).
     *
     * @param request The verification request containing the specialist's email and MFA code.
     * @return A response indicating the successful login and providing JWT tokens.
     * @throws InvalidLoginException If the MFA code is invalid.
     */
    public LoginResponse verifySpecialistLogin(VerificationRequest request) {
        dcLogger.info("Verifying login for specialist: {}", request.getEmail());
        Specialist specialist = authHelpers.returnSpecialistIfPresent(request.getEmail());

        boolean isGoogle2FAValid = google2FAuthentication.isOtpValid(specialist.getMfaSecret(), request.getCode());
        if (isGoogle2FAValid){
            dcLogger.warn("Invalid Google OTP for specialist: {}", request.getEmail());
        } else if (email2FAuthentication.verifyOtp(request)) {
            dcLogger.info("OTP verified for specialist: {}", request.getEmail());
        } else {
            // If neither Google 2FA nor Email OTP is valid, throw an exception
            dcLogger.warn("Invalid Email OTP for specialist: {}", request.getEmail());
            throw new InvalidLoginException(AuthenticationError.builder()
                    .path(authHelpers.getRequest().getRequestURI())
                    .error("Invalid OTP.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        dcLogger.info("Generating access/refresh token for specialist: {}", specialist.getEmail());
        String accessToken = jwtService.generateToken(specialist);
        String refreshToken = jwtService.generateRefreshToken(specialist);
        authHelpers.revokeUserTokens(specialist);
        authHelpers.saveJwtTokenToRepository(specialist, accessToken);

        return authHelpers.createLoginResponse(
                specialist,
                "Specialist logged in successfully.",
                accessToken,
                refreshToken,
                null,
                true);
    }

    /**
     * Sends a two-factor authentication (2FA) code to a specialist via email for verification.
     *
     * @param verificationRequest The verification request containing the specialist's email.
     * @return A response indicating the successful sending of the 2FA code.
     */
    public VerificationResponse sendVerificationCodeToSpecialist(VerificationRequest verificationRequest) {
        dcLogger.info("Sending 2FA code to specialist: {}", verificationRequest.getEmail());
        Specialist specialist = authHelpers.returnSpecialistIfPresent(verificationRequest.getEmail());
        email2FAuthentication.sendOtp(specialist, "Generic Message: DocConnect 2FA");

        return authHelpers.createVerificationResponse(
                "2FA code sent successfully!",
                verificationRequest.getEmail(),
                HttpStatus.OK);
    }

    /**
     * Sends a forgot password email to a specialist and initiates the password reset process.
     *
     * @param request The forgot password request containing the specialist's email.
     * @return A response indicating the successful sending of the password reset email.
     */
    public ForgotPasswordResponse sendSpecialistForgotPasswordEmail(@Valid ForgotPasswordRequest request){
        dcLogger.info("Sending forgot password email to specialist: {}", request.getEmailAddress());

        Specialist specialist = authHelpers.returnSpecialistIfPresent(request.getEmailAddress());
        PasswordToken passwordToken = PasswordToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .specialist(specialist)
                .build();

        dcLogger.info("Saving password token to database for specialist: {}", specialist.getEmail());
        passwordTokenRepository.save(passwordToken);

        dcLogger.info("Sending password reset email to specialist: {}", specialist.getEmail());
        // Should redirect to front-end page for better understand how does it work
        emailSender.send(specialist.getEmail(), buildConfirmationEmail(specialist.getFirstName() + " " + specialist.getLastName()
                        , CONFIRMATION_LINK + passwordToken.getToken()),
                "DocConnect: Reset your password");

        return authHelpers.createForgotPasswordResponse(
                "Password reset email sent successfully!" + passwordToken.getToken(),
                specialist.getEmail(),
                HttpStatus.OK);
    }

    /**
     * Resets the password for a specialist based on a password reset token.
     *
     * @param request The reset password request containing the password reset token and the new password.
     * @return A response indicating the successful password reset.
     */
    public ForgotPasswordResponse resetSpecialistPassword(@Valid ResetPasswordRequest request){
        dcLogger.info("Resetting password for specialist with token: {}", request.getToken());

        PasswordToken passwordToken = authHelpers.validatePasswordTokenAndProceed(request.getToken());

        Specialist specialist = passwordToken.getSpecialist();
        specialist.setPassword(passwordEncoder.encode(request.getResetPassword()));

        specialistRepository.save(specialist);
        return authHelpers.createForgotPasswordResponse(
                "Password reset successfully!",
                specialist.getEmail(),
                HttpStatus.OK);
    }

    /**
     * Retrieves information about a specialist with the given ID and calculates the average rating.
     *
     * @param specialistId The unique identifier of the specialist to retrieve.
     * @return A {@link SpecialistResponse} object containing specialist details and average rating.
     * @throws SpecialistNotFoundException If the specialist with the provided ID is not found.
     */
    public SpecialistResponse returnASpecialist(Long specialistId) {
        dcLogger.info("Returning specialist with ID: {}", specialistId);

        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> {
                    dcLogger.warn("Specialist not found for ID: {}", specialistId);
                    throw new SpecialistNotFoundException(AuthenticationError.builder()
                            .path(authHelpers.getRequest().getRequestURI())
                            .error("Specialist not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });

        List<Rating> specialistRatings = ratingRepository.findAllBySpecialistId(specialist.getId());

        dcLogger.debug("Returned specialist with ID {}: {}", specialistId, specialist.getFirstName() + " " + specialist.getLastName());
        Double averageRating = specialistRatings.stream().mapToDouble(Rating::getRatingSize).average().orElse(0.0);

        return buildSpecialResponse(specialist, averageRating);
    }

    /**
     * Searches for specialists based on specified filters, and returns a pageable list of SpecialistResponse objects.
     *
     * @param cityId      The ID of the city to filter specialists by (can be null for no city filter).
     * @param name        The name or part of the name of specialists to search for (can be null for no name filter).
     * @param specialtyId The ID of the specialty to filter specialists by (can be null for no specialty filter).
     * @param pageable    The paging and sorting configuration for the results.
     * @return A pageable list of {@link SpecialistResponse} objects matching the search criteria.
     */
    public Page<SpecialistResponse> searchDoctorsPageable(Long cityId, String name, Long specialtyId, Pageable pageable) {
        try {
            Page<Object[]> specialistsPage = specialistRepository.searchDoctorsPageable(name, specialtyId, cityId, pageable);

            Page<SpecialistResponse> specialistsDtoPage = specialistsPage.map(row -> {
                Specialist specialist = (Specialist) row[0];
                Double averageRating = (Double) row[1];

                SpecialistResponse specialistDto = modelMapper.map(specialist, SpecialistResponse.class);
                if (averageRating == null) averageRating = 0.0;
                specialistDto.setRating(averageRating);
                return buildSpecialResponse(specialist, averageRating);
            });

            dcLogger.debug("Retrieved {} specialists with filters: cityId={}, name={}, specialtyId={}, pageSize={}, pageNum={}",
                    specialistsDtoPage.getTotalElements(), cityId, name, specialtyId, pageable.getPageSize(), pageable.getPageNumber());
            return specialistsDtoPage;
        } catch (Exception e) {
            dcLogger.error("Failed to search specialists Message: {}, Cause: {}"
                    , e, e.getMessage(), e.getCause());
            return Page.empty();
        }
    }

    private SpecialistResponse buildSpecialResponse(Specialist specialist, Double averageRating){
        return SpecialistResponse.builder()
                .id(specialist.getId())
                .firstName(specialist.getFirstName())
                .lastName(specialist.getLastName())
                .phoneNumber(specialist.getPhoneNumber())
                .email(specialist.getEmail())
                .summary(specialist.getSummary())
                .experienceYears(specialist.getExperienceYears())
                .city(modelMapper.map(specialist.getCity(), CityResponse.class))
                .country(modelMapper.map(specialist.getCountry(), CountryResponse.class))
                .specialty(modelMapper.map(specialist.getSpecialty(), SpecialtyResponse.class))
                .imageUrl(specialist.getImageUrl())
                .addresses(specialist.getAddresses().stream()
                        .map(address -> modelMapper.map(address, SpecialistAddressResponse.class))
                        .toList())
                .rating(averageRating)
                .build();
    }
}
