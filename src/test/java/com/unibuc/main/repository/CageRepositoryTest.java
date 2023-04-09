package com.unibuc.main.repository;

import com.unibuc.main.entity.Cage;
import com.unibuc.main.utils.CageMocks;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
public class CageRepositoryTest {

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addCageTest() {
        Cage cage = CageMocks.mockCage();
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        cageRepository.save(cage);
    }

    @Test
    @Order(2)
    public void findByCageIdTest() {
        Optional<Cage> cage = cageRepository.findById(1L);
        assertFalse(cage.isEmpty());
        assertEquals(cage.get().getId(), 1L);
        assertEquals(cage.get().getNumberPlaces(), 3);
    }
}
