package com.parunev.docconnect.config;

import com.parunev.docconnect.security.SpringSecurityAuditorAware;
import com.parunev.docconnect.security.oauth2.OAuthProperties;
import com.parunev.docconnect.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableConfigurationProperties(value = {RsaConfig.class, OAuthProperties.class})
public class ApplicationConfig {

    private final UserService userService;

    // @Value("${azure.communication.endpoint}")
    private String communicationEndpoint;

    // @Value("${azure.communication.credential}")
    private String communicationCredential;

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

//    @Bean
//    public EmailClient emailClient(){
//        return new EmailClientBuilder()
//                .endpoint(communicationEndpoint)
//                .credential(new AzureKeyCredential(communicationCredential))
//                .buildClient();
//    }
}
