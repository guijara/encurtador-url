package com.guilherme.encurtador_url.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private SecurityFilter securityFilter;
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final RateLimitingFilter rateLimitingFilter;

    public SecurityConfig(SecurityFilter securityFilter,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          RateLimitingFilter rateLimitingFilter){
        this.securityFilter = securityFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.rateLimitingFilter = rateLimitingFilter;
    }

    // Gerencia as permissões dos endpoints da aplicação
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/{shortUrl}").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
//                        .anyRequest().authenticated()
//                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(rateLimitingFilter, SecurityFilter.class)
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/{shortUrl}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                // 1. Colocamos o Rate Limit bem no início, após o tratamento de CORS
                .addFilterAfter(rateLimitingFilter, org.springframework.web.filter.CorsFilter.class)

                // 2. Colocamos o seu filtro de JWT antes do filtro de login padrão
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
