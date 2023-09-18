package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.specialist.SpecialistAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link SpecialistAddress} entity.
 */
@Repository
public interface SpecialistAddressRepository extends JpaRepository<SpecialistAddress, Long> {
}
