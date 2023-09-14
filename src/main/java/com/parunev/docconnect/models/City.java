package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
/**
 * The `City` entity class represents a city within the application. It contains
 * information about the city name, the country to which it belongs, and a set
 * of users associated with the city.
 * <p>
 * Entity Attributes:
 * - `cityName`: A string representing the name of the city. It is limited to
 *   50 characters and is non-nullable.
 * - `country`: A many-to-one relationship with the `Country` entity, indicating
 *   the country to which the city belongs.
 * - `user`: A one-to-many relationship with the `User` entity, representing the
 *   set of users associated with the city.
 * <p>
 * Entity Relationships:
 * - Many cities can belong to one country, established through the `country`
 *   attribute.
 * - Many users can be associated with one city, established through the `user`
 *   attribute.
 * <p>
 * Usage:
 * The `City` entity can be used to store and retrieve information about cities
 * in the application. You can associate cities with countries and users as
 * needed.
 * <p>
 * Example:
 * ```java
 * // Create a new city and associate it with a country
 * City city = new City();
 * city.setCityName("New York");
 * city.setCountry(someCountry); // Assign an existing Country entity
 * cityRepository.save(city);
 * <p>
 * // Retrieve users associated with a city
 * Set<User> cityUsers = city.getUsers();
 * ```
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.OneToMany
 * @see jakarta.persistence.JoinColumn
 * @see com.parunev.docconnect.models.commons.BaseEntity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "CITIES")
@AttributeOverride(name = "id", column = @Column(name = "CITY_ID"))
public class City extends BaseEntity {

    /**
     * The name of the city.
     */
    @Column(name = "CITY_NAME", length = 50, nullable = false)
    private String cityName;

    /**
     * The country to which the city belongs, established through a many-to-one
     * relationship.
     */
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    /**
     * The set of users associated with the city, established through a
     * one-to-many relationship.
     */
    @OneToMany(mappedBy = "city")
    private Set<User> user;
}
