package com.unibuc.main.repository;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.entity.Client;
import com.unibuc.main.utils.ClientMocks;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ClientRepositoryTest {
    
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Order(1)
    public void addClientTest() {
        Client client = ClientMocks.mockClient();
        clientRepository.save(client);
    }

    @Test
    @Order(2)
    public void findClientByNameTest() {
        Optional<Client> client = clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertFalse(client.isEmpty());
        log.info("Client: " + client.get());
        assertEquals(client.get().getId(), 1L);
        assertEquals(client.get().getPersonDetails().getFirstName(), TestConstants.FIRSTNAME);
        assertEquals(client.get().getPersonDetails().getLastName(), TestConstants.LASTNAME);
    }

    @Test
    @Order(3)
    public void avgAgeTest() {
        Double avgAge = clientRepository.avgAge();
        assertEquals(avgAge, 22);
    }
}
