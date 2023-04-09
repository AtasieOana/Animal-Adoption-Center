package com.unibuc.main.repository;

import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
public class MedicalRecordRepositoryTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private DietRepository dietRepository;

    @Test
    @Order(1)
    public void addMedicalRecordTest() {
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        cageRepository.save(CageMocks.mockCage());
        dietRepository.save(DietMocks.mockDiet());
        animalRepository.save(AnimalMocks.mockAnimal());
        animalRepository.save(AnimalMocks.mockAnimal2());
        employeeRepository.save(EmployeeMocks.mockVet());

        MedicalRecord medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordRepository.save(medicalRecord);

        MedicalRecord medicalRecord2 = MedicalRecordMocks.mockMedicalRecord2();
        medicalRecordRepository.save(medicalRecord2);
    }

    @Test
    @Order(2)
    public void findMedicalRecordsByAnimalsIdTest() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByAnimalsId(Arrays.asList(1L, 2L));
        assertFalse(medicalRecords.isEmpty());
        medicalRecords.forEach(mr ->  assertTrue(Arrays.asList(1L, 2L).contains(mr.getAnimal().getId())));
        assertEquals(medicalRecords.size(), 2);
    }
}
