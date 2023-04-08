package com.unibuc.main.repository;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Log
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.animal.id in :animalIds")
    List<MedicalRecord> findMedicalRecordsByAnimalsId(List<Long> animalIds);

}
