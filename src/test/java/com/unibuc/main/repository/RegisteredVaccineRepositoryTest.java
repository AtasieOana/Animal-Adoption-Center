package com.unibuc.main.repository;

import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RegisteredVaccineRepositoryTest {

    @Autowired
    private RegisteredVaccineRepository registeredVaccineRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Test
    @Order(1)
    public void addRegisteredVaccineTest() {
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        employeeRepository.save(EmployeeMocks.mockVet());
        cageRepository.save(CageMocks.mockCage());
        dietRepository.save(DietMocks.mockDiet());
        animalRepository.save(AnimalMocks.mockAnimal());
        animalRepository.save(AnimalMocks.mockAnimal2());
        vaccineRepository.save(VaccineMocks.mockVaccine());
        medicalRecordRepository.save(MedicalRecordMocks.mockMedicalRecord());

        RegisteredVaccine registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineRepository.save(registeredVaccine);
    }

    @Test
    @Order(2)
    public void findByMedicalRecordIdTest() {
        List<RegisteredVaccine> registeredVaccines = registeredVaccineRepository.findByMedicalRecordId(1L);
        assertFalse(registeredVaccines.isEmpty());
        registeredVaccines.forEach(rv ->  assertEquals(rv.getMedicalRecord().getId(), 1L));
        assertEquals(registeredVaccines.size(), 1);
    }

    @Test
    @Order(3)
    public void findByMedicalRecordIdAndVaccineIdTest() {
        Optional<RegisteredVaccine> registeredVaccine = registeredVaccineRepository.findByMedicalRecordIdAndVaccineId(1L, 1L);
        assertFalse(registeredVaccine.isEmpty());
        log.info("Registered Vaccine: " + registeredVaccine.get());
        assertEquals(registeredVaccine.get().getVaccine().getId(), 1L);
        assertEquals(registeredVaccine.get().getMedicalRecord().getId(), 1L);
    }
}
