package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The `UserRepository` interface provides access to the persistence layer
 * for managing `User` entities. It extends the Spring Data JPA
 * `JpaRepository` interface, which provides basic CRUD (Create, Read, Update,
 * Delete) operations for the `User` entity.
 * <p>
 * Usage:
 * You can use the methods provided by this repository to interact with the
 * `User` entities stored in the database. For example:
 * <p>
 * ```java
 * // Find a user by their ID
 * User user = userRepository.findById(1L).orElse(null);
 * <p>
 * // Save a new user to the database
 * userRepository.save(newUser);
 * <p>
 * // Delete a user from the database
 * userRepository.deleteById(2L);
 * ```
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.parunev.docconnect.models.User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
