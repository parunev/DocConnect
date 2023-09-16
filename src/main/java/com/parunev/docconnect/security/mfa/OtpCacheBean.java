package com.parunev.docconnect.security.mfa;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.parunev.docconnect.config.OtpConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class responsible for creating a Guava-based LoadingCache
 * for storing One-Time Password (OTP) values with a specified expiration time.
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OtpConfig.class)
public class OtpCacheBean {

    private final OtpConfig otpConfig;

    /**
     * Creates a LoadingCache for storing OTP values.
     *
     * @return A LoadingCache instance.
     */
    @Bean
    public LoadingCache<String, Integer> loadingCache() {
        final int expirationMinutes = otpConfig.getExpirationMinutes();

        return configureCacheBuilder(expirationMinutes).build(new CacheLoader<>() {

            /**
             * Loads the OTP value associated with the given key.
             *
             * @param key The key for which to load the OTP value.
             * @return The OTP value (0 in case of cache miss).
             */
            @NonNull
            public Integer load(@NonNull String key) {
                return 0;
            }
        });
    }


    /**
     * Configures a CacheBuilder with the specified expiration time in minutes.
     *
     * @param expirationMinutes The expiration time for cache entries in minutes.
     * @return A CacheBuilder configured with the specified expiration time.
     */
    private CacheBuilder<Object, Object> configureCacheBuilder(int expirationMinutes) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(expirationMinutes, TimeUnit.MINUTES);
    }
}

