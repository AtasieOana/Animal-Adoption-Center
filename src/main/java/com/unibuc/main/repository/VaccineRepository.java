package com.unibuc.main.repository;

import com.unibuc.main.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    @Query("SELECT v FROM Vaccine v ORDER BY v.expirationDate ASC")
    List<Vaccine> findAllOrderByExpirationDate();

    List<Vaccine> findAllByExpirationDateBefore(Date date);

    @Query("SELECT v FROM Vaccine v WHERE v.quantityOnStock = 0")
    List<Vaccine> findAllVaccinesWithEmptyStock();

    Optional<Vaccine> findByVaccineName(String name);
}
