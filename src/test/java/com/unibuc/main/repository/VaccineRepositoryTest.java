package com.unibuc.main.repository;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.utils.VaccineMocks;
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
public class VaccineRepositoryTest {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Test
    @Order(1)
    public void addVaccinesTest() {
        Vaccine vaccine = VaccineMocks.mockVaccine();
        vaccineRepository.save(vaccine);
        Vaccine vaccine2 = VaccineMocks.mockVaccine2();
        vaccineRepository.save(vaccine2);
    }

    @Test
    @Order(2)
    public void findAllOrderByExpirationDateTest() {
        List<Vaccine> vaccineList = vaccineRepository.findAllOrderByExpirationDate();
        log.info("Vaccine list: " + vaccineList);
        assertFalse(vaccineList.isEmpty());
        assertEquals(vaccineList.get(0).getVaccineName(), TestConstants.VACCINE_NAME);
        assertEquals(vaccineList.size(), 2);
    }

    @Test
    @Order(3)
    public void findAllByExpirationDateBeforeTest() {
        List<Vaccine> vaccineList = vaccineRepository.findAllByExpirationDateBefore();
        log.info("Vaccine list: " + vaccineList);
        assertFalse(vaccineList.isEmpty());
        assertEquals(vaccineList.get(0).getVaccineName(), TestConstants.VACCINE_NAME);
        assertEquals(vaccineList.size(), 1);
    }

    @Test
    @Order(4)
    public void findByVaccineNameTest() {
        Optional<Vaccine> vaccine = vaccineRepository.findByVaccineName(TestConstants.VACCINE_NAME);
        assertFalse(vaccine.isEmpty());
        log.info("Vaccine: " + vaccine.get());
        assertEquals(vaccine.get().getVaccineName(), TestConstants.VACCINE_NAME);
    }
}
