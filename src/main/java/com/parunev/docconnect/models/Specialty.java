package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * The `Specialty` class represents a medical specialty or field of expertise.
 * It extends the `BaseEntity` class, inheriting basic entity properties such as ID and creation/update timestamps.
 * This entity stores information about medical specialties, including the name of the specialty
 * and an image URL that may be associated with it.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SPECIALTIES")
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "SPECIALTY_ID"))
public class Specialty extends BaseEntity {

    /**
     * The name of the medical specialty.
     * It is a non-null string that can be up to 100 characters in length.
     */
    @Column(name = "SPECIALTY_NAME", length = 100, nullable = false)
    private String specialtyName;

    /**
     * The URL to an image representing the medical specialty.
     * It is a non-null string, typically used for visual identification of the specialty.
     */
    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;
}
