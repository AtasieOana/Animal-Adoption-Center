package com.unibuc.main.repository;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    @Log
    @Query("SELECT v FROM Vaccine v ORDER BY v.expirationDate ASC")
    List<Vaccine> findAllOrderByExpirationDate();

    @Log
    @Query("SELECT v FROM Vaccine v WHERE v.expirationDate < CURRENT_DATE")
    List<Vaccine> findAllByExpirationDateBefore();

    @Log
    Optional<Vaccine> findByVaccineName(String name);
}
