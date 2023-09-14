package com.parunev.docconnect.models.commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * The `BaseEntity` class serves as the base entity for all persistent entities
 * within the application. It defines common attributes and behavior that are
 * inherited by other entity classes.
 * <p>
 * This class is marked as `@MappedSuperclass`, indicating that it is not
 * meant to be directly instantiated as an entity, but rather used as a
 * blueprint for other entity classes.
 * <p>
 * Attributes:
 * - `id`: A unique identifier for the entity. It is annotated with `@Id` to
 *         specify that it is the primary key, and `@GeneratedValue` to
 *         indicate that the value is generated automatically using an
 *         identity strategy. The `@Setter(AccessLevel.NONE)` annotation
 *         restricts direct modification of the `id` field.
 * <p>
 * Usage:
 * To create a new entity, extend the `BaseEntity` class and define any
 * additional attributes specific to that entity. For example:
 * <p>
 * ```java
 * @Entity
 * public class User extends BaseEntity {
 *     private String username;
 *     private String email;
 *     // ... other attributes and methods
 * }
 * ```
 *
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see jakarta.persistence.Id
 * @see jakarta.persistence.MappedSuperclass
 * @see lombok.Data
 * @see lombok.Setter
 */

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class BaseEntity extends Auditable<String>{

    /**
     * The unique identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
}
