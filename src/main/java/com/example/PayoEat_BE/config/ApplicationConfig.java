package com.example.PayoEat_BE.config;

import com.example.PayoEat_BE.security.jwt.AuthTokenFilter;
import com.example.PayoEat_BE.security.jwt.JwtAuthEntryPoint;
import com.example.PayoEat_BE.security.jwt.JwtUtils;
import com.example.PayoEat_BE.security.user.AuthUserDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationConfig {
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtUtils jwtUtils;
    private final JwtAuthEntryPoint authEntryPoint;

    public ApplicationConfig(AuthUserDetailsService authUserDetailsService, JwtUtils jwtUtils, JwtAuthEntryPoint authEntryPoint) {
        this.authUserDetailsService = authUserDetailsService;
        this.jwtUtils = jwtUtils;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter(jwtUtils, authUserDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = new ArrayList<>(List.of(
                "https://m8t547vr-5173.asse.devtunnels.ms",
                "http://localhost:5173",
                "https://*.ngrok-free.dev"
        ));

        configuration.setAllowedOriginPatterns(origins);

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/confirm").permitAll()
                        .requestMatchers("/api/auth/forget-password", "/api/auth/reset-password", "/api/auth/check-email").permitAll()
                        .requestMatchers("/api/restaurant/all", "/api/restaurant/detail/**", "/api/restaurant/similar-restaurants").permitAll()
                        .requestMatchers("/api/restaurant/register-restaurant", "/api/restaurant/check-restaurant-name").permitAll()
                        .requestMatchers("/api/restaurant-category/all", "/api/restaurant-category/get-by-id").permitAll()
                        .requestMatchers("/api/menu/get-menus", "/api/menu/get-menus-by-restaurant", "/api/menu/get-menu-by-code").permitAll()
                        .requestMatchers("/api/review/get", "/api/review/get-restaurant-review-stats").permitAll()
                        .requestMatchers("/api/order/place", "/api/order/add-payment-proof").permitAll()
                        .requestMatchers("/api/order/history/customer", "/api/order/reviewable").permitAll()
                        .requestMatchers("/api/order/details-order-by-customer").permitAll()
                        .requestMatchers("/api/review/add").permitAll()
                        .requestMatchers("/api/order/progress").permitAll()
                        .requestMatchers("/api/order/check-payment", "/api/order/payment-modal-data", "/api/order/qr").permitAll()
                        .requestMatchers("/api/order/confirm-redirect", "/api/order/confirm2").permitAll()
                        .requestMatchers("/api/search/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                );
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}