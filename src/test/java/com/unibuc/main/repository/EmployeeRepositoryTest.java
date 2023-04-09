package com.unibuc.main.repository;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.utils.EmployeeMocks;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
public class EmployeeRepositoryTest {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addEmployeeTest() {
        Employee caretaker = EmployeeMocks.mockCaretaker();
        employeeRepository.save(caretaker);
        Employee vet = EmployeeMocks.mockVet();
        employeeRepository.save(vet);
        Employee caretaker2 = EmployeeMocks.mockCaretaker2();
        employeeRepository.save(caretaker2);
        Employee vet2 = EmployeeMocks.mockVet2();
        employeeRepository.save(vet2);
    }

    @Test
    @Order(2)
    public void findCaretakerByNameTest() {
        Optional<Employee> caretaker = employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertFalse(caretaker.isEmpty());
        log.info("Caretaker: " + caretaker.get());
        assertEquals(caretaker.get().getId(), 1L);
        assertEquals(caretaker.get().getPersonDetails().getFirstName(), TestConstants.FIRSTNAME);
        assertEquals(caretaker.get().getPersonDetails().getLastName(), TestConstants.LASTNAME);
    }

    @Test
    @Order(3)
    public void findVetByNameTest() {
        Optional<Employee> vet = employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
        assertFalse(vet.isEmpty());
        log.info("Vet: " + vet.get());
        assertEquals(vet.get().getId(), 2L);
        assertEquals(vet.get().getPersonDetails().getFirstName(), TestConstants.FIRSTNAME);
        assertEquals(vet.get().getPersonDetails().getLastName(), TestConstants.LASTNAME_VET);
    }

    @Test
    @Order(4)
    public void findAllByResponsibilityNotNullTest() {
        List<Employee> caretakers = employeeRepository.findAllByResponsibilityNotNull();
        assertFalse(caretakers.isEmpty());
        log.info("Caretakers: " + caretakers);
        caretakers.forEach(caretaker ->  assertNotNull(caretaker.getResponsibility()));
        caretakers.forEach(caretaker ->  assertNull(caretaker.getExperience()));
        assertEquals(caretakers.size(), 2);
    }

    @Test
    @Order(5)
    public void findAllByExperienceNotNullTest() {
        List<Employee> vets = employeeRepository.findAllByExperienceNotNull();
        assertFalse(vets.isEmpty());
        log.info("Vets: " + vets);
        vets.forEach(caretaker ->  assertNotNull(caretaker.getExperience()));
        vets.forEach(caretaker ->  assertNull(caretaker.getResponsibility()));
        assertEquals(vets.size(), 2);
    }
}
