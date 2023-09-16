package com.parunev.docconnect.security.mfa;

import com.google.common.cache.LoadingCache;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.payloads.user.login.VerificationRequest;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.OtpValidationException;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class Email2FAuthentication {
    private final LoadingCache<String, Integer> otpCache;
    private final UserRepository userRepository;
    private final DCLogger dcLogger = new DCLogger(Email2FAuthentication.class);
    private final EmailSender emailSender;
    private final Random random = new Random();

    /**
     * Send a one-time password (OTP) to the user's email address.
     * @param user The user for whom to send the OTP.
     * @param subject The subject of the email.
     */
    public void sendOtp(final User user, final String subject) {
        try {
            dcLogger.info("Removing OTP from cache for user: {}", user.getEmail());
            otpCache.get(user.getEmail());
            otpCache.invalidate(user.getEmail());
        } catch (ExecutionException e) {
            dcLogger.error("Failed to fetch pair from OTP cache: {}", e);
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

        final var otp = generateRandomOtp();
        otpCache.put(user.getEmail(), otp);

        CompletableFuture.supplyAsync(() -> {
            dcLogger.info("Sending OTP to user: {}", user.getEmail());
            emailSender.send(user.getEmail(), subject, "OTP: " + otp);
            return HttpStatus.OK;
        });
    }

    /**
     * Generate a random OTP using the shared Random instance.
     * @return The generated OTP.
     */
    private int generateRandomOtp() {
        return random.ints(1, 100000, 999999).sum();
    }

    /**
     * Verify the OTP entered by the user.
     * @param verificationRequest The verification request containing user's email and OTP.
     * @return True if the OTP is valid, false otherwise.
     */
    public boolean verifyOtp(final VerificationRequest verificationRequest){
        dcLogger.info("Verifying OTP for user: {}", verificationRequest.getEmail());
        User user = userRepository.findByEmail(verificationRequest.getEmail())
                .orElseThrow();

        Integer storedOneTimePassword;

        try{
            dcLogger.info("Fetching OTP from cache for user: {}", user.getEmail());
            storedOneTimePassword = otpCache.get(user.getEmail());
        } catch (ExecutionException e) {
            dcLogger.error("Failed to fetch pair from OTP cache: {}", e);
            throw new OtpValidationException("Failed to fetch OTP from cache.");
        }

        return storedOneTimePassword.equals(Integer.parseInt(verificationRequest.getCode()));
    }

}
