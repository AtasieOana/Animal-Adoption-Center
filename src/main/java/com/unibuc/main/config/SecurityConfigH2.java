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
                .password(passwordEncoder.encode("pass123"))
                .roles("EMPLOYEE")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("pass123"))
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
                        .antMatchers("/animals/add", "/animals/deleteAdoptedAnimals","/animals/edit/{animalId}","/animals/adoptAnimal/{animalId}").permitAll()
                        .antMatchers("/animals", "/animals/{animalId}", "/animals/getOldestAnimalInCenter").permitAll()
                        .antMatchers("/cages/add", "/cages/delete/{cageId}","/cages/edit/{cageId}","/cages/updateCage/{cageId}").permitAll()
                        .antMatchers("/cages", "/cages/{cageId}").permitAll()
                        .antMatchers("/diets/add", "/diets/deleteIfStockEmpty/{dietType}","/diets/edit/{oldDietType}","/diets/updateDiet/{oldDietType}").permitAll()
                        .antMatchers("/diets", "/diets/{dietType}").permitAll()
                        .antMatchers("/caretakers/add", "/caretakers/delete/{firstName}/{lastName}","/caretakers/edit/{oldFirstName}/{oldLastName}","/caretakers/updateCaretaker/{oldFirstName}/{oldLastName}").permitAll()
                        .antMatchers("/caretakers", "/caretakers/{firstName}/{lastName}").permitAll()
                        .antMatchers("/vets/add", "/vets/delete/{firstName}/{lastName}","/vets/edit/{oldFirstName}/{oldLastName}","/vets/updateVet/{oldFirstName}/{oldLastName}").permitAll()
                        .antMatchers("/vets", "/vets/{firstName}/{lastName}").permitAll()
                        .antMatchers("/clients/add", "/clients/delete/{firstName}/{lastName}","/clients/edit/{oldFirstName}/{oldLastName}","/clients/updateClient/{oldFirstName}/{oldLastName}").permitAll()
                        .antMatchers("/clients", "/clients/{firstName}/{lastName}").permitAll()
                        .antMatchers("/vaccines/add", "/vaccines/deleteExpiredVaccines","/vaccines/edit/{oldVaccineName}","/vaccines/updateVaccine/{oldVaccineName}").permitAll()
                        .antMatchers("/vaccines", "/vaccines/{vaccineType}").permitAll()
                        .antMatchers("/medicalRecords/add", "/medicalRecords/delete/{medicalRecordId}","/medicalRecords/edit/{medicalRecordId}","/medicalRecords/updateMedicalRecord/{medicalRecordId}").permitAll()
                        .antMatchers("/medicalRecords", "/medicalRecords/{medicalRecordId}").permitAll()
                        .antMatchers("/registeredVaccines/add", "/registeredVaccines/delete/{medicalRecordId}","/registeredVaccines/edit/{medicalRecordId}","/registeredVaccines/updateRegisteredVaccine/{medicalRecordId}").permitAll()
                        .antMatchers("/registeredVaccines", "/registeredVaccines/{medicalRecordId}").permitAll()
                        .antMatchers("/auction").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().sameOrigin())
                .httpBasic(withDefaults())
                .build();
    }




}

