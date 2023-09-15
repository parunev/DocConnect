package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The `CountryRepository` interface provides access to the persistence layer
 * for managing `Country` entities. It extends the Spring Data JPA
 * `JpaRepository` interface, which provides basic CRUD (Create, Read, Update,
 * Delete) operations for the `Country` entity.
 * <p>
 * Usage:
 * You can use the methods provided by this repository to interact with the
 * `Country` entities stored in the database. For example:
 * <p>
 * ```java
 * // Find a country by its ID
 * Country country = countryRepository.findById(1L).orElse(null);
 * <p>
 * // Save a new country to the database
 * countryRepository.save(newCountry);
 * <p>
 * // Delete a country from the database
 * countryRepository.deleteById(2L);
 * ```
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.parunev.docconnect.models.Country
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCountryName(String countryName);
}
