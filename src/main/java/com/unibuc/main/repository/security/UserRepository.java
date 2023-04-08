package com.unibuc.main.repository.security;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Log
    Optional<User> findByUsername(String username);
}
