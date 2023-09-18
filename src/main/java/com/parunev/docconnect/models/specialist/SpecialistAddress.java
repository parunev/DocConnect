package com.parunev.docconnect.models.specialist;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * This class represents the entity of the specialist address.
 * The class is used by Hibernate to create a table in the database.
 * The class contains the following fields:
 * - docAddress - the address of the specialist
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SPECIALISTS_ADDRESSES")
@AttributeOverride(name = "id", column = @Column(name = "SPECIALIST_ADDR_ID"))
public class SpecialistAddress extends BaseEntity {

    /**
     * The address of the specialist.
     */
    @Column(name = "SPECIALIST_ADDRESS", length = 500)
    private String docAddress;
}
