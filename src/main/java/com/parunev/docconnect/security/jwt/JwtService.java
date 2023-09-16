package com.parunev.docconnect.security.jwt;

import com.parunev.docconnect.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * A service class responsible for JWT (JSON Web Token) generation and validation.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    /**
     * Extract the email from a JWT token.
     *
     * @param token The JWT token from which to extract the email.
     * @return The email extracted from the token.
     */
    public String extractEmail(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    /**
     * Generate a JWT access token for a user.
     *
     * @param userDetails The user for whom to generate the token.
     * @return The generated JWT access token.
     */
    public String generateToken(User userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate a JWT refresh token for a user.
     *
     * @param userDetails The user for whom to generate the token.
     * @return The generated JWT refresh token.
     */
    public String generateRefreshToken(User userDetails){
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Generate a JWT token for a user with additional claims.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user for whom to generate the token.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims,
                                User userDetails){
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Build a JWT token with the specified claims, user details, and expiration.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user for whom to generate the token.
     * @param expiration The token expiration duration in milliseconds.
     * @return The generated JWT token.
     */
    public String buildToken(Map<String, Object> extraClaims,
                             User userDetails,
                             long expiration) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", userDetails.getRole().getAuthority());
        claims.putAll(extraClaims);

        JwtClaimsSet.Builder jwtClaimsSetBuilder = JwtClaimsSet.builder()
                .subject(userDetails.getEmail())
                .issuer("DocConnect_API")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMillis(expiration)));

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            jwtClaimsSetBuilder.claim(entry.getKey(), entry.getValue());
        }

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetBuilder.build())).getTokenValue();
    }

    /**
     * Extract the expiration timestamp from a JWT token.
     *
     * @param token The JWT token from which to extract the expiration timestamp.
     * @return The expiration timestamp as an Instant.
     */
    public Instant extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    /**
     * Extract a specific claim from a JWT token.
     *
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolver A function to resolve the specific claim from the token's claims.
     * @param <T> The type of the extracted claim.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.claims(extractClaim(token));
        claims.setExpiration(Date.from(Objects.requireNonNull(jwtDecoder.decode(token).getExpiresAt())));
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from a JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return A map of extracted claims.
     */
    public Map<String, Object> extractClaim(String token) {
        return jwtDecoder.decode(token).getClaims();
    }

    /**
     * Check if a JWT token is valid for a specific user.
     *
     * @param token The JWT token to validate.
     * @param userDetails The UserDetails representing the user.
     * @return true if the token is valid for the user, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        return (extractEmail(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Check if a JWT token has expired.
     *
     * @param token The JWT token to check.
     * @return true if the token has expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(Instant.now());
    }
}
