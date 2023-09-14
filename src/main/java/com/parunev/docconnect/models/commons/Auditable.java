package com.parunev.docconnect.models.commons;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

/**
 * The `Auditable` class is an abstract class that provides common auditing
 * fields for entities in a Spring Data JPA-based application. It uses Spring
 * Data JPA's auditing annotations to automatically manage and populate audit
 * information such as creation and modification timestamps and user identifiers.
 * <p>
 * Class Structure:
 * - An abstract class that can be extended by other entity classes to inherit
 *   auditing behavior.
 * - Uses the `@MappedSuperclass` annotation to indicate that it is a superclass
 *   for other entity classes but does not represent a standalone entity.
 * - Configures entity listeners with `@EntityListeners(AuditingEntityListener.class)`
 *   to enable auditing.
 * <p>
 * Auditing Fields:
 * - `createdBy`: Represents the user who created the entity.
 * - `createDate`: Represents the timestamp when the entity was created.
 * - `lastModifiedBy`: Represents the user who last modified the entity.
 * - `lastModifiedDate`: Represents the timestamp of the last modification
 *   to the entity.
 * <p>
 * Usage:
 * 1. Extend this class in your entity classes to inherit auditing behavior.
 *    For example:
 *    ```java
 *    @Entity
 *    public class YourEntity extends Auditable<String> {
 *        // Other entity attributes and methods
 *    }
 *    ```
 * 2. Configure Spring Data JPA to enable auditing in your application by using
 *    `@EnableJpaAuditing` or equivalent configuration.
 * <p>
 * Example:
 * ```java
 * // Enable Spring Data JPA auditing in your application configuration.
 * @Configuration
 * @EnableJpaAuditing
 * public class JpaConfig {
 *     // Additional configuration
 * }
 * ```
 *
 * @param <U> The type representing the user or identifier for auditing.
 *
 * @see jakarta.persistence.MappedSuperclass
 * @see jakarta.persistence.EntityListeners
 * @see org.springframework.data.annotation.CreatedBy
 * @see org.springframework.data.annotation.CreatedDate
 * @see org.springframework.data.annotation.LastModifiedBy
 * @see org.springframework.data.annotation.LastModifiedDate
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    /**
     * The user who created the entity.
     */
    @CreatedBy
    @Column(name = "CREATED_BY", length = 200)
    protected U createdBy;

    /**
     * The timestamp when the entity was created.
     */
    @CreatedDate
    @Column(name = "TIMESTAMP_CREATED")
    protected LocalDate createDate;

    /**
     * The user who last modified the entity.
     */
    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 200)
    protected U lastModifiedBy;

    /**
     * The timestamp of the last modification to the entity.
     */
    @LastModifiedDate
    @Column(name = "TIMESTAMP_UPDATED")
    protected LocalDate lastModifiedDate;
}
