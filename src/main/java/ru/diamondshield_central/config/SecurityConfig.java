package ru.diamondshield_central.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ru.diamondshield_central.security.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Включаем CORS в Spring Security
                .cors(Customizer.withDefaults())

                // Для JWT обычно CSRF отключают
                .csrf(csrf -> csrf.disable())

                // Stateless, так как авторизация через JWT, а не через session/cookie
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // Preflight-запросы браузера должны проходить без авторизации
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // API локальных серверов защищается собственным токеном в заголовках
                        .requestMatchers("/api/local-sync/**").permitAll()

                        // WebSocket endpoint разрешаем
                        .requestMatchers("/ws/**").permitAll()

                        // Авторизация/логин/регистрация
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/public/guest-requests").permitAll()

                        // Все остальные endpoints требуют JWT
                        .anyRequest().authenticated()


                )

                // JWT-фильтр ставим перед UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        /*
         * Если JWT передается в заголовке:
         * Authorization: Bearer token
         *
         * allowCredentials(false) подходит.
         *
         * Если будешь использовать cookie/session и на фронте withCredentials: true,
         * тогда надо поставить true.
         */
        configuration.setAllowCredentials(false);

        /*
         * Нужно, если фронт должен читать Authorization из response headers.
         * Например, если backend возвращает новый JWT в заголовке Authorization.
         */
        configuration.setExposedHeaders(List.of("Authorization"));

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
    /**
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    **/
}