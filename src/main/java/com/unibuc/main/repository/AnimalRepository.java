package com.unibuc.main.repository;

import com.unibuc.main.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByCage_Id(Long cageId);

    List<Animal> findAllByClientIsNotNull();

    @Query("SELECT a FROM Animal a WHERE a.foundDate = (SELECT MIN(m.foundDate) FROM Animal m)")
    List<Animal> findOldestAnimal();
}
