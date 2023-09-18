package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The `RatingRepository` interface is responsible for providing data access methods
 * to interact with the `Rating` entity in the database. It extends the JpaRepository,
 * which provides common CRUD (Create, Read, Update, Delete) operations.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllBySpecialistId(Long specialistId);
    @Query("SELECT AVG(r.ratingSize) FROM RATING r WHERE r.specialist.id = :specialistId")
    Double getAverageRatingBySpecialistId(Long specialistId);

    Page<Rating> findAllBySpecialistId(Long specialistId, Pageable pageable);
}
