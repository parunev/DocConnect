package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.ConfirmationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The `ConfirmationTokenRepository` interface is a Spring Data JPA repository for the `ConfirmationToken` entity.
 * It provides methods for performing CRUD (Create, Read, Update, Delete) operations on `ConfirmationToken` objects
 * in the database.
 * This repository includes custom query methods to find a `ConfirmationToken` by its associated token string
 * and to update the confirmation timestamp.
 *
 */
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    /**
     * Retrieves a `ConfirmationToken` entity by its associated token string.
     *
     * @param token The token string to search for.
     * @return An optional `ConfirmationToken` object if found, or an empty optional if not found.
     */
    Optional<ConfirmationToken> findByToken(String token);

    /**
     * Updates the confirmation timestamp for a `ConfirmationToken` with the specified token.
     *
     * @param token The token string of the confirmation token to update.
     * @param now   The timestamp to set as the confirmation time.
     */
    @Transactional
    @Modifying
    @Query("UPDATE CONFIRMATION_TOKENS c SET c.confirmedAt = ?2 WHERE c.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime now);

}
