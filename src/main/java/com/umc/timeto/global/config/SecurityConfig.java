package com.umc.timeto.global.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Getter
    private final List<String> requireAuthUrls = List.of(
            "/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html",
            "/swagger-ui/index.html", "/swagger-resources/**","/webjars/**"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll() // POST 요청 중 "/register", "/login"은 허용
                        //.requestMatchers(HttpMethod.GET, "/", "/health-check").permitAll() // GET 요청 중 "/"는 허용
                        //.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS 요청 모두 허용
                        .requestMatchers(requireAuthUrls.toArray(new String[0])).permitAll() // Swagger 관련 URL 허용
                        .anyRequest().permitAll()  // 로그인 구현 후 authenticated()로 수정
                )
                .formLogin(login -> login.disable())
                .logout(logout -> logout.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 미사용
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:5173", "http://localhost:3000", "http://localhost:8080"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization", "Content-Type"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        var src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
