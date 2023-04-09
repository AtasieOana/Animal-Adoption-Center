package com.unibuc.main.config;

import com.unibuc.main.entity.security.Authority;
import com.unibuc.main.entity.security.User;
import com.unibuc.main.repository.security.AuthorityRepository;
import com.unibuc.main.repository.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("mysql")
public class DataLoader implements CommandLineRunner {

    private AuthorityRepository authorityRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private void loadUserData() {
        if (userRepository.count() == 0){
            Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
            Authority guestRole = authorityRepository.save(Authority.builder().role("ROLE_EMPLOYEE").build());

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .authority(adminRole)
                    .build();

            User guest = User.builder()
                    .username("employee")
                    .password(passwordEncoder.encode("password"))
                    .authority(guestRole)
                    .build();

            userRepository.save(admin);
            userRepository.save(guest);
        }
    }


    @Override
    public void run(String... args) {
        loadUserData();
    }
}
