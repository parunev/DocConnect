package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The `CityRepository` interface provides access to the persistence layer
 * for managing `City` entities. It extends the Spring Data JPA
 * `JpaRepository` interface, which provides basic CRUD (Create, Read, Update,
 * Delete) operations for the `City` entity.
 * <p>
 * Usage:
 * You can use the methods provided by this repository to interact with the
 * `City` entities stored in the database. For example:
 * <p>
 * ```java
 * // Find a city by its ID
 * City city = cityRepository.findById(1L).orElse(null);
 * <p>
 * // Save a new city to the database
 * cityRepository.save(newCity);
 * <p>
 * // Delete a city from the database
 * cityRepository.deleteById(2L);
 * ```
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.parunev.docconnect.models.City
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
