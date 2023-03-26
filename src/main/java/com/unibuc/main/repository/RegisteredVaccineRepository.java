package com.unibuc.main.repository;

import com.unibuc.main.entity.RegisteredVaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredVaccineRepository extends JpaRepository<RegisteredVaccine, Long> {
}
