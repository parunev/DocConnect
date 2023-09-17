package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The `PasswordTokenRepository` interface is a Spring Data JPA repository for the `PasswordToken` entity.
 * It provides methods for performing CRUD (Create, Read, Update, Delete) operations on `PasswordToken` objects
 * in the database.
 * This repository includes a custom query method to find a `PasswordToken` by its associated token.
 */
@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

    /**
     * Retrieves a `PasswordToken` entity by its associated token string.
     *
     * @param token The token string to search for.
     * @return An optional `PasswordToken` object if found, or an empty optional if not found.
     */
    Optional<PasswordToken> findByToken(String token);
}
