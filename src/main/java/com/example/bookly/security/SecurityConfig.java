package com.example.bookly.security;

import com.example.bookly.security.JwtAuthFilter;
import com.example.bookly.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // بنعطل الـ CSRF لأن الـ REST APIs بتستخدم tokens مش sessions
                .csrf(AbstractHttpConfigurer::disable)

                // مش هنخزن session — كل request لازم يجيب الـ token بتاعه
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // ━━ Public endpoints — أي حد يقدر يوصلهم ━━
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // الكتب والأوثورز والكاتيجوريز — القراءة public
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/authors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()

                        // ━━ ADMIN only — الكتابة والحذف ━━
                        .requestMatchers(HttpMethod.POST,   "/api/v1/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,   "/api/v1/authors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/authors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/authors/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,   "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("ADMIN")

                        // يوزرز — الأدمن بس يشوف الكل ويمسح
                        .requestMatchers(HttpMethod.GET,    "/api/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")

                        // أوردرز — الأدمن يشوف الكل، اليوزر يشوف بتاعه بس
                        .requestMatchers(HttpMethod.GET,    "/api/v1/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/v1/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ━━ أي حاجة تانية — لازم يكون logged in ━━
                        .anyRequest().authenticated()
                )

                // بنحط الـ JwtAuthFilter قبل الـ filter الافتراضي بتاع Spring
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // BCrypt — بيشفر الباسورد
    // مش بيخزن الباسورد كـ plain text أبداً
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}