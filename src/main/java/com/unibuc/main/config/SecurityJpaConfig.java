package com.unibuc.main.config;

import com.unibuc.main.service.security.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("mysql")
public class SecurityJpaConfig {

    private final JpaUserDetailsService userDetailsService;

    public SecurityJpaConfig(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                                .antMatchers("/animals/add", "/animals/deleteAdoptedAnimals","/animals/edit/{animalId}","/animals/adoptAnimal/{animalId}").hasRole("ADMIN")
                                .antMatchers("/animals", "/animals/{animalId}", "/animals/getOldestAnimalInCenter").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/cages/add", "/cages/delete/{cageId}","/cages/edit/{cageId}","/cages/updateCage/{cageId}").hasRole("ADMIN")
                                .antMatchers("/cages", "/cages/{cageId}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/diets/add", "/diets/deleteIfStockEmpty/{dietType}","/diets/edit/{oldDietType}","/diets/updateDiet/{oldDietType}").hasRole("ADMIN")
                                .antMatchers("/diets", "/diets/{dietType}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/caretakers/add", "/caretakers/delete/{firstName}/{lastName}","/caretakers/edit/{oldFirstName}/{oldLastName}","/caretakers/updateCaretaker/{oldFirstName}/{oldLastName}").hasRole("ADMIN")
                                .antMatchers("/caretakers", "/caretakers/{firstName}/{lastName}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/vets/add", "/vets/delete/{firstName}/{lastName}","/vets/edit/{oldFirstName}/{oldLastName}","/vets/updateVet/{oldFirstName}/{oldLastName}").hasRole("ADMIN")
                                .antMatchers("/vets", "/vets/{firstName}/{lastName}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/clients/add", "/clients/delete/{firstName}/{lastName}","/clients/edit/{oldFirstName}/{oldLastName}","/clients/updateClient/{oldFirstName}/{oldLastName}").hasRole("ADMIN")
                                .antMatchers("/clients", "/clients/{firstName}/{lastName}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/vaccines/add", "/vaccines/deleteExpiredVaccines","/vaccines/edit/{oldVaccineName}","/vaccines/updateVaccine/{oldVaccineName}").hasRole("ADMIN")
                                .antMatchers("/vaccines", "/vaccines/{vaccineType}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/medicalRecords/add", "/medicalRecords/delete/{medicalRecordId}","/medicalRecords/edit/{medicalRecordId}","/medicalRecords/updateMedicalRecord/{medicalRecordId}").hasRole("ADMIN")
                                .antMatchers("/medicalRecords", "/medicalRecords/{medicalRecordId}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/registeredVaccines/add", "/registeredVaccines/delete/{medicalRecordId}","/registeredVaccines/edit/{medicalRecordId}","/registeredVaccines/updateRegisteredVaccine/{medicalRecordId}").hasRole("ADMIN")
                                .antMatchers("/registeredVaccines", "/registeredVaccines/{medicalRecordId}").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/auction").hasAnyRole("ADMIN", "EMPLOYEE")
                                .antMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access_denied")
                .and()
                .httpBasic(withDefaults())
                .build();
    }


}


