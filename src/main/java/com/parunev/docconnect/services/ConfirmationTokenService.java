package com.parunev.docconnect.services;

import com.parunev.docconnect.models.ConfirmationToken;
import com.parunev.docconnect.repositories.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * A service class responsible for managing confirmation tokens.
 */
@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;

    /**
     * Save a confirmation token to the repository.
     *
     * @param confirmationToken The confirmation token to be saved.
     */
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        tokenRepository.save(confirmationToken);
    }

    /**
     * Get a confirmation token by its token value.
     *
     * @param token The token value to search for.
     * @return An Optional containing the found confirmation token, if any.
     */
    public Optional<ConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    /**
     * Set the confirmedAt timestamp for a confirmation token.
     *
     * @param token The token for which to set the confirmedAt timestamp.
     */
    public void setConfirmedAt(String token) {
        tokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

}
