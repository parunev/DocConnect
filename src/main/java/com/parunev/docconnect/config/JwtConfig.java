package com.parunev.docconnect.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * The {@code JwtConfig} class is responsible for configuring JSON Web Token (JWT) encoding and decoding in a Spring-based application.
 * It defines beans for JWT decoder and encoder, using Nimbus-Jose-JWT library, and makes use of RSA keys provided by the {@code RsaConfig}.
 *
 * <p>The {@code jwtDecoder()} bean configures a JWT decoder that can be used to verify and decode JWT tokens using a public RSA key.
 * It specifies the signature algorithm as RS256 for token verification.
 *
 * <p>The {@code jwtEncoder()} bean configures a JWT encoder that can be used to create and sign JWT tokens using a private RSA key.
 * It builds a JSON Web Key (JWK) from the public and private RSA keys and creates a JWK source for encoding JWTs.
 *
 * @see RsaConfig
 */
@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    /**
     * The configuration for RSA keys used in JWT encoding and decoding.
     */
    private final RsaConfig rsaConfig;

    /**
     * Creates and configures a JWT decoder for token verification.
     *
     * @return The configured JWT decoder.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaConfig.publicKey()).signatureAlgorithm(SignatureAlgorithm.RS256).build();
    }

    /**
     * Creates and configures a JWT encoder for token creation and signing.
     *
     * @return The configured JWT encoder.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        // Build a JSON Web Key (JWK) from RSA keys and create a JWK source for encoding JWTs.
        JWK jwk = new RSAKey.Builder(rsaConfig.publicKey())
                .privateKey(rsaConfig.privateKey())
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

}
