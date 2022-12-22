package com.unibuc.main.repository;

import com.unibuc.main.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    Optional<Diet> findByDietType(String name);

    @Query("SELECT d FROM Diet d WHERE d.quantityOnStock = 0")
    List<Diet> findAllDietsWithEmptyStock();

    @Modifying
    @Transactional
    @Query("UPDATE Diet d SET d.quantityOnStock = :quantityOnStock, d.dietType = :dietType WHERE d.id = :id")
    void updateDiet(Long id, String dietType, Integer quantityOnStock);
}
