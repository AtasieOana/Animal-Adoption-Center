package com.unibuc.main.controller.IT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.entity.Client;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.utils.ClientMocks;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@ActiveProfiles("h2")
@Slf4j
public class ClientControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
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
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getClientsPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/clients?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/clientPaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getClientByNameTest() throws Exception {
        mockMvc.perform(get("/clients/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/clientDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addClientFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/clients/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/addClientForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveClientTest() throws Exception {
        mockMvc.perform(post("/clients").with(csrf()).
                        flashAttr("client", ClientMocks.mockClientDto2()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editClientFormTest() throws Exception {
        mockMvc.perform(get("/clients/edit/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/editClientForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editClientTest() throws Exception {
        mockMvc.perform(post("/clients/updateClient/{oldFirstName}/{oldLastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME).with(csrf()).
                        flashAttr("client", ClientMocks.mockPartialClientDto()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteClientTest() throws Exception {
        //THEN
        mockMvc.perform(get("/clients/delete/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
    }
}
