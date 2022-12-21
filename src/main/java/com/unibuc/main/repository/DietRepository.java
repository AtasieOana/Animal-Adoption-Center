package com.unibuc.main.repository;

import com.unibuc.main.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    Optional<Diet> findByDietType(String name);

    @Query("SELECT d FROM Diet d WHERE d.quantityOnStock = 0")
    List<Diet> findAllDietsWithEmptyStock();
}
