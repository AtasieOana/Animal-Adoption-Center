package com.unibuc.main.repository;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.entity.Animal;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
public class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addAnimalsTest() {
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        cageRepository.save(CageMocks.mockCage());
        dietRepository.save(DietMocks.mockDiet());
        clientRepository.save(ClientMocks.mockClient());

        Animal animal = AnimalMocks.mockAnimal();
        animalRepository.save(animal);

        Animal animal2 = AnimalMocks.mockAnimal2();
        animalRepository.save(animal2);

        Animal animal3 = AnimalMocks.mockAnimalWithClient();
        animalRepository.save(animal3);
    }

    @Test
    @Order(2)
    public void findAllByCageIdTest() {
        List<Animal> animals = animalRepository.findAllByCageId(1L);
        log.info("Animal list in cage: " + animals);
        assertFalse(animals.isEmpty());
        assertEquals(animals.get(0).getAnimalType(), TestConstants.ANIMAL_TYPE);
        assertEquals(animals.size(), 2);
    }

    @Test
    @Order(3)
    public void findAllByClientIsNotNullTest() {
        List<Animal> animals = animalRepository.findAllByClientIsNotNull();
        log.info("Animal list adopted: " + animals);
        assertFalse(animals.isEmpty());
        animals.forEach(animal ->  assertNotNull(animal.getClient()));
        animals.forEach(animal ->  assertNull(animal.getCage()));
        assertEquals(animals.size(), 1);
    }

    @Test
    @Order(4)
    public void findAllByClientIsNullTest() {
        List<Animal> animals = animalRepository.findAllByClientIsNull();
        log.info("Animal list not adopted: " + animals);
        assertFalse(animals.isEmpty());
        animals.forEach(animal ->  assertNotNull(animal.getCage()));
        animals.forEach(animal ->  assertNull(animal.getClient()));
        assertEquals(animals.size(), 2);
    }

    @Test
    @Order(5)
    public void findOldestAnimalTest() throws ParseException {
        List<Animal> animals = animalRepository.findOldestAnimal();
        log.info("Oldest animals: " + animals);
        assertFalse(animals.isEmpty());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date_string = "26-09-2022";
        Date date = formatter.parse(date_string);
        assertEquals(animals.get(0).getFoundDate().getTime(), date.getTime());
        assertEquals(animals.size(), 1);
    }
}
