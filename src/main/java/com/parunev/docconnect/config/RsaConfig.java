package com.parunev.docconnect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * The {@code RsaConfig} record is responsible for configuring RSA (Rivest-Shamir-Adleman) key pairs used in a Spring-based application.
 * It uses Spring Boot's `ConfigurationProperties` annotation to bind properties with the prefix "rsa" to the fields of this record.
 *
 * <p>The {@code publicKey} property represents the RSA public key used for various cryptographic operations.
 * The {@code privateKey} property represents the RSA private key used for cryptographic operations requiring private key access.
 *
 * @param publicKey The RSA public key used in the application.
 * @param privateKey The RSA private key used in the application.
 *
 * @see ConfigurationProperties
 */
@ConfigurationProperties(prefix = "rsa")
public record RsaConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
