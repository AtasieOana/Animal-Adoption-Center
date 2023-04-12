package com.unibuc.main.repository;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.entity.Diet;
import com.unibuc.main.utils.DietMocks;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DietRepositoryTest {

    @Autowired
    private DietRepository dietRepository;

    @Test
    @Order(1)
    public void addDietTest() {
        Diet diet = DietMocks.mockDiet();
        dietRepository.save(diet);
    }

    @Test
    @Order(2)
    public void findByDietTypeTest() {
        Optional<Diet> diet = dietRepository.findByDietType(TestConstants.DIET_NAME);
        assertFalse(diet.isEmpty());
    }
}
