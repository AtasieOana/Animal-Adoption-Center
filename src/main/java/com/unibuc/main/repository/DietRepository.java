package com.unibuc.main.repository;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    @Log
    Optional<Diet> findByDietType(String name);

}
