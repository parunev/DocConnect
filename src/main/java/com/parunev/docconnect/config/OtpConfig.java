package com.parunev.docconnect.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The {@code OtpConfig} class is responsible for configuring properties related to One-Time Password (OTP) functionality in a Spring-based application.
 * It uses Spring Boot's `ConfigurationProperties` annotation to bind properties with the prefix "docconect.otp" to the fields of this class.
 *
 * <p>The {@code expirationMinutes} property specifies the duration, in minutes, for which an OTP remains valid before it expires.
 * This configuration is used to control the validity period of OTPs generated for user authentication.
 *
 * @see ConfigurationProperties
 */
@Data
@ConfigurationProperties(prefix = "docconect.otp")
public class OtpConfig {

    /**
     * The duration, in minutes, for which an OTP remains valid before it expires.
     */
    private final Integer expirationMinutes;
}
