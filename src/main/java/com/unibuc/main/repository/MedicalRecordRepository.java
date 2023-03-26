package com.unibuc.main.repository;

import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findAllByAnimal_Id(Long animalId);

    List<MedicalRecord> findAllByGenerationDateBefore(Date generationDate);

}
