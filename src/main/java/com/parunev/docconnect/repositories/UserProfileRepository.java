package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@code UserProfileRepository} interface is responsible for defining data access operations
 * for managing user profiles in the database. It extends the JpaRepository interface provided by
 * Spring Data JPA and is designed to work with entities of type {@link UserProfile}.
 *
 * <p>By extending JpaRepository, this repository interface inherits common CRUD (Create, Read,
 * Update, Delete) operations for managing user profile entities.
 *
 * @see UserProfile
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long id);
}
