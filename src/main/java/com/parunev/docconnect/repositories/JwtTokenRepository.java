package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The `JwtTokenRepository` interface provides access to the persistence layer
 * for managing `JwtToken` entities. It extends the Spring Data JPA
 * `JpaRepository` interface, which provides basic CRUD (Create, Read, Update,
 * Delete) operations for the `JwtToken` entity.
 * <p>
 * Usage:
 * You can use the methods provided by this repository to interact with the
 * `JwtToken` entities stored in the database. For example:
 * <p>
 * ```java
 * // Find a JWT token by its ID
 * JwtToken token = jwtTokenRepository.findById(1L).orElse(null);
 * <p>
 * // Save a new JWT token to the database
 * jwtTokenRepository.save(newToken);
 * <p>
 * // Delete a JWT token from the database
 * jwtTokenRepository.deleteById(2L);
 * ```
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.parunev.docconnect.models.JwtToken
 */
@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Query("""
        SELECT J FROM JWT_TOKENS J INNER JOIN USERS U ON J.user.id = U.id
        WHERE U.id = :userId AND (J.isExpired = false OR J.isRevoked = false)
    """)
    List<JwtToken> findAllValidTokensByUser(Long userId);

    Optional<JwtToken> findByToken(String token);
}
