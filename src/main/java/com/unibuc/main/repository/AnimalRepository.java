package com.unibuc.main.repository;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Log
    List<Animal> findAllByCage_Id(Long cageId);
    @Log
    List<Animal> findAllByClientIsNotNull();
    @Log
    List<Animal> findAllByClientIsNull();
    @Log
    @Query("SELECT a FROM Animal a WHERE a.foundDate = (SELECT MIN(m.foundDate) FROM Animal m)")
    List<Animal> findOldestAnimal();
}
