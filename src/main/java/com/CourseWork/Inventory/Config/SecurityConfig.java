package com.CourseWork.Inventory.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // ✅ Дає доступ до всіх сторінок без авторизації
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // вимикає CSRF-захист (щоб працювали POST-форми без токена)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // дозволяє всі запити
                )
                .formLogin(form -> form.disable()) // вимикає стандартну форму логіну Spring
                .httpBasic(basic -> basic.disable()); // вимикає базову авторизацію

        return http.build();
    }

    // ✅ Підключає BCrypt для хешування паролів
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}