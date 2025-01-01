package com.zaid.journalApp.config;
import com.zaid.journalApp.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SpringSecurity {
    private final UserDetailsServiceImpl userDetailsService;

    public SpringSecurity(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
        log.info("initializing SpringSecurity");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring Spring Security filter chain");
        http
                .authorizeRequests(authorizeRequests -> {
                    log.debug("Configuring authorization rules");
                    authorizeRequests
                            .requestMatchers("/journals/**", "/users/**").authenticated() // Authenticate all journal-related endpoints
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll();
                })
                .httpBasic(Customizer.withDefaults()) // Replaces deprecated `httpBasic()` method
                .csrf(csrf -> csrf.disable()) // Removed extra semicolon
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ); // Updated session management configuration
        log.info("Configured Spring Security filter chain completed");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        log.debug("Creating authentication manager bean");
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating password encoder bean");
        return new BCryptPasswordEncoder();
    }
}