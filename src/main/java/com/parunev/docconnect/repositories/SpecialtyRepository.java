package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The `SpecialtyRepository` interface is a Spring Data JPA repository for the `Specialty` entity.
 * It provides methods for performing CRUD (Create, Read, Update, Delete) operations on `Specialty` objects
 * in the database.
 * This repository does not include any custom query methods and relies on the default Spring Data JPA
 * repository methods for basic CRUD operations.
 */
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsBySpecialtyName(String specialtyName);
}
