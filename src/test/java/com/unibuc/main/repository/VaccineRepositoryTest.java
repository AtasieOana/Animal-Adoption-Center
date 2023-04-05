package com.unibuc.main.repository;

import com.unibuc.main.entity.Vaccine;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class VaccineRepositoryTest {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Test
    @Order(1)
    public void addVaccine1Test() throws ParseException {
        Vaccine vaccine = new Vaccine();
        vaccine.setVaccineName("Rabies");
        vaccine.setQuantityOnStock(10);
        String date_string = "26-09-2021";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(date_string);
        vaccine.setExpirationDate(date);
        vaccineRepository.save(vaccine);
    }

    @Test
    @Order(2)
    public void addVaccine2Test() throws ParseException {
        Vaccine vaccine = new Vaccine();
        vaccine.setVaccineName("Arbovirus");
        vaccine.setQuantityOnStock(20);
        String date_string = "26-10-2024";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(date_string);
        vaccine.setExpirationDate(date);
        vaccineRepository.save(vaccine);
    }

    @Test
    @Order(3)
    public void findAllOrderByExpirationDateTest() {
        List<Vaccine> vaccineList = vaccineRepository.findAllOrderByExpirationDate();
        log.info("Vaccine list: " + vaccineList);
        assertFalse(vaccineList.isEmpty());
        assertEquals(vaccineList.get(0).getVaccineName(), "Rabies");
        assertEquals(vaccineList.size(), 2);
    }

    @Test
    @Order(4)
    public void findAllByExpirationDateBeforeTest() {
        List<Vaccine> vaccineList = vaccineRepository.findAllByExpirationDateBefore();
        log.info("Vaccine list: " + vaccineList);
        assertFalse(vaccineList.isEmpty());
        assertEquals(vaccineList.get(0).getVaccineName(), "Rabies");
        assertEquals(vaccineList.size(), 1);
    }

    @Test
    @Order(5)
    public void findByVaccineNameTest() {
        Optional<Vaccine> vaccine = vaccineRepository.findByVaccineName("Rabies");
        assertFalse(vaccine.isEmpty());
        log.info("Vaccine: " + vaccine.get());
        assertEquals(vaccine.get().getVaccineName(), "Rabies");
    }
}
