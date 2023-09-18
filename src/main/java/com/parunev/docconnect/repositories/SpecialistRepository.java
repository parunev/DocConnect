package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.specialist.Specialist;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The `SpecialistRepository` interface provides access to the persistence layer
 * for managing `Specialist` entities. It extends the Spring Data JPA
 * `JpaRepository` interface, which provides basic CRUD (Create, Read, Update,
 * Delete) operations for the `Specialist` entity.
 * Similar to the `UserRepository` interface
 */
@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    boolean existsByEmail(String email);

    Optional<Specialist> findByEmail(String emailAddress);

    @Transactional
    @Modifying
    @Query("UPDATE SPECIALISTS s SET s.isEnabled = TRUE WHERE s.email = ?1")
    void enableSpecialist(String specialistEmail);


    @Query("SELECT s, AVG(r.ratingSize) as averageRating FROM SPECIALISTS s " +
            "LEFT JOIN RATING r ON s.id = r.specialist.id " +
            "WHERE (:specialistName IS NULL OR " +
            "LOWER(s.firstName) LIKE concat(:specialistName, '%') or " +
            "LOWER(s.lastName) LIKE concat(:specialistName, '%')) " +
            "AND (:cityId IS NULL OR s.city.id = :cityId) " +
            "AND (:specialtyId IS NULL OR s.specialty.id = :specialtyId) " +
            "GROUP BY s.id")
    Page<Object[]> searchDoctorsPageable(
            @Param("specialistName") String specialistName,
            @Param("specialtyId") Long specialtyId,
            @Param("cityId") Long cityId,
            Pageable pageable);
}
