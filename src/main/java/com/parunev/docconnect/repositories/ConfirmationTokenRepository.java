package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.ConfirmationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE CONFIRMATION_TOKENS c SET c.confirmedAt = ?2 WHERE c.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime now);

}
