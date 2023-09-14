package com.parunev.docconnect.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The `SpringSecurityAuditorAware` class implements the `AuditorAware` interface
 * to provide a mechanism for determining the current auditor (typically, the user
 * responsible for an action) within the Spring Security context.
 * <p>
 * This class retrieves the currently authenticated user's username from the
 * Spring Security context and returns it as the current auditor. If no user is
 * authenticated, it provides a default auditor identifier, which in this case is
 * "DOC_CONNECT."
 * <p>
 * Class Structure:
 * - Implements the Spring Data JPA `AuditorAware` interface.
 * - Provides an implementation for the `getCurrentAuditor` method.
 * <p>
 * Usage:
 * This class is used within an application that utilizes Spring Security and
 * Spring Data JPA to determine the current auditor, which can be associated with
 * actions such as record creation or modification.
 * <p>
 * Example:
 * ```java
 * // Configure Spring Data JPA to use the `SpringSecurityAuditorAware` class
 * // as the auditor provider in your application configuration.
 * @Configuration
 * @EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
 * public class JpaConfig {
 *     @Bean
 *     public AuditorAware<String> springSecurityAuditorAware() {
 *         return new SpringSecurityAuditorAware();
 *     }
 * }
 * ```
 *
 * @see org.springframework.data.domain.AuditorAware
 * @see org.springframework.security.core.Authentication
 * @see org.springframework.security.core.context.SecurityContextHolder
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {


    /**
     * Retrieves the currently authenticated user's username from the Spring
     * Security context or provides a default value if no user is authenticated.
     *
     * @return An `Optional` containing the current auditor's username, or an
     *         `Optional` with the default auditor identifier ("DOC_CONNECT").
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null){
            String username = authentication.getName();
            return Optional.ofNullable(username).filter(s -> !s.isEmpty());
        } else {
            return Optional.of("DOC_CONNECT");
        }
    }
}