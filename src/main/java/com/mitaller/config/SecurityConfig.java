package com.mitaller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } //

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/buscar", "/talleres/**", "/registro", "/login",
                        "/css/**", "/js/**", "/images/**").permitAll() // publico
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") // rol
                .anyRequest().authenticated() //autenticar
            )
            .formLogin(form -> form
                .loginPage("/login") // url login
                .defaultSuccessUrl("/", true) //homepage autenticada
                .failureUrl("/login?error=true") // vuelve al login con parametro de error
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true") //vuelve al login
                .permitAll()
            );

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")); //no carga datos en iframes externos
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
