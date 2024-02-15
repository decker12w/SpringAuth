package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.securityjwt.JwtConfigurer;
import com.example.demo.securityjwt.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

        @Autowired
        private JwtTokenProvider tokenProvider;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                
                http.httpBasic(AbstractHttpConfigurer::disable)
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(
                                                session -> session
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(
                                                authorizeHttpRequests -> authorizeHttpRequests
                                                                .requestMatchers(
                                                                                "/v3/api-docs",
                                                                                "/configuration/ui",
                                                                                "/swagger-resources/**",
                                                                                "/configuration/security",
                                                                                "/swagger-ui.html",
                                                                                "/swagger-ui/*",
                                                                                "/webjars/**",
                                                                                "/v3/**",
                                                                                "/auth/signin",
                                                                                "/auth/refresh",
                                                                                "/api-docs/**",
                                                                                "/swagger-ui/**")
                                                                .permitAll()
                                                                .requestMatchers("/api/**").authenticated()
                                                                .requestMatchers("/users").denyAll())
                                .cors(Customizer.withDefaults())
                                .apply(new JwtConfigurer(tokenProvider));
                return http.build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                Map<String, PasswordEncoder> encoders = new HashMap<>();

                Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000,
                                SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
                encoders.put("pbkdf2", pbkdf2Encoder);
                DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
                passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
                return passwordEncoder;
        }

        @Bean
        AuthenticationManager authenticationManagerBean(
                        AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}