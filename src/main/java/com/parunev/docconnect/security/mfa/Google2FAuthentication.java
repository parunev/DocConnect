package com.parunev.docconnect.security.mfa;

import com.parunev.docconnect.utils.DCLogger;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * The {@code Google2FAuthenticationService} class provides functionality for working with
 * Google Authenticator-based two-factor authentication (2FA) in the application.
 * It handles operations such as generating new 2FA secrets, creating QR code images for
 * setup, and validating OTP (One-Time Password) codes.
 */
@Service
public class Google2FAuthentication {
    private final DCLogger dcLogger = new DCLogger(Google2FAuthentication.class);

    /**
     * Generates a new secret key for two-factor authentication (2FA).
     *
     * @return A generated secret key as a string.
     */
    public String generateNewSecret() {
        dcLogger.info("Generating new secret for 2FA");
        return new DefaultSecretGenerator().generate();
    }

    /**
     * Generates a Data URI for a QR code image containing setup information for 2FA.
     *
     * @param secret The 2FA secret key.
     * @return A Data URI representing the QR code image.
     */
    public String generateQrCodeImageUri(String secret) {
        // Construct QR code data with the provided secret and additional information.
        // Generate a QR code image and return it as a Data URI.
        // Log success or error messages as appropriate.

        QrData data = new QrData.Builder()
                .label("DocConnect 2FA")
                .secret(secret)
                .issuer("DocConnect-API")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try {
            imageData = generator.generate(data);
            dcLogger.info("QR-CODE generated successfully: {}");
        } catch (QrGenerationException e) {
            dcLogger.error("Error while generating QR-CODE", e, e.getMessage());
        }

        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    /**
     * Validates an OTP (One-Time Password) code against a given 2FA secret.
     *
     * @param secret The 2FA secret key.
     * @param code   The OTP code to validate.
     * @return {@code true} if the OTP code is valid, {@code false} otherwise.
     */
    public boolean isOtpValid(String secret, String code) {
        // Validate the OTP code against the provided secret.
        // Use time-based verification to ensure the code is valid within the time window.
        // Log validation results and messages.

        dcLogger.info("Validating OTP code for secret: {}", secret);
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}
