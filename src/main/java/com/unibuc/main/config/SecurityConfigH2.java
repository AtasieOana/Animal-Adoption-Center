package com.unibuc.main.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("h2")
public class SecurityConfigH2{


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("employee")
                .password(passwordEncoder.encode("password"))
                .roles("EMPLOYEE")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("EMPLOYEE", "ADMIN")
                .build());
        return manager;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringAntMatchers("/h2-console/**"))
                .authorizeRequests(auth -> auth
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/animals/**").permitAll()
                        .antMatchers("/cages/**").permitAll()
                        .antMatchers("/diets/**").permitAll()
                        .antMatchers("/caretakers/**").permitAll()
                        .antMatchers("/vets/**").permitAll()
                        .antMatchers("/clients/**").permitAll()
                        .antMatchers("/vaccines/**").permitAll()
                        .antMatchers("/medicalRecords/**").permitAll()
                        .antMatchers("/registeredVaccines/**").permitAll()
                        .antMatchers("/auction").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().sameOrigin())
                .httpBasic(withDefaults())
                .build();
    }




}

