package com.unibuc.main.repository;

import com.unibuc.main.entity.RegisteredVaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredVaccineRepository extends JpaRepository<RegisteredVaccine, Long> {

    List<RegisteredVaccine> findByMedicalRecordId(Long medicalRecordId);

    Optional<RegisteredVaccine> findByMedicalRecordIdAndVaccineId(Long medicalRecordId, Long vaccineId);


}
