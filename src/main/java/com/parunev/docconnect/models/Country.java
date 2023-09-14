package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * The `Country` entity class represents a country within the application. It
 * contains information about the country name and associations with cities and
 * users.
 * <p>
 * Entity Attributes:
 * - `countryName`: A string representing the name of the country. It is limited
 *   to 50 characters and is non-nullable.
 * - `cities`: A one-to-many relationship with the `City` entity, representing
 *   the set of cities associated with the country.
 * - `users`: A one-to-many relationship with the `User` entity, representing the
 *   set of users associated with the country.
 * <p>
 * Entity Relationships:
 * - One country can have many cities, established through the `cities`
 *   attribute.
 * - One country can have many users, established through the `users` attribute.
 * <p>
 * Usage:
 * The `Country` entity can be used to store and retrieve information about
 * countries in the application. You can associate countries with cities and
 * users as needed.
 * <p>
 * Example:
 * ```java
 * // Create a new country and associate it with cities and users
 * Country country = new Country();
 * country.setCountryName("United States");
 * country.setCities(someCities); // Assign a Set of City entities
 * country.setUsers(someUsers); // Assign a Set of User entities
 * countryRepository.save(country);
 * <p>
 * // Retrieve cities associated with a country
 * Set<City> countryCities = country.getCities();
 * ```
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.OneToMany
 * @see jakarta.persistence.JoinColumn
 * @see com.parunev.docconnect.models.commons.BaseEntity
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "COUNTRIES")
@AttributeOverride(name = "id", column = @Column(name = "COUNTRY_ID"))
public class Country extends BaseEntity {

        /**
         * The name of the country.
         */
        @Column(name = "COUNTRY_NAME", length = 50, nullable = false)
        private String countryName;

        /**
         * The set of cities associated with the country, established through a
         * one-to-many relationship.
         */
        @OneToMany(mappedBy = "country")
        private Set<City> cities;

        /**
         * The set of users associated with the country, established through a
         * one-to-many relationship.
         */
        @OneToMany(mappedBy = "country")
        private Set<User> user;
}
