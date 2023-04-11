package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.ClientController;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.service.ClientService;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class ClientControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ClientService clientService;
    @MockBean
    Model model;
    ClientDto clientDto;
    PartialClientDto partialClientDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getClientsPageMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();
        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(clientService.findPaginatedClients(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(clientDtos));

        //THEN
        mockMvc.perform(get("/clients?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/clientPaginated"))
                .andExpect(model().attribute("clientPage", new PageImpl<>(clientDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(clientService, times(1)).findPaginatedClients(PageRequest.of(currentPage - 1, pageSize));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getClientByNameMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(clientDto);

        //THEN
        mockMvc.perform(get("/clients/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/clientDetails"))
                .andExpect(model().attribute("client", clientDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(clientService, times(1)).getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addClientFormMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/clients/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/addClientForm"))
                .andExpect(model().attribute("client", new ClientDto()))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveClientMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.addNewClient(clientDto)).thenReturn(clientDto);

        //THEN
        mockMvc.perform(post("/clients").with(csrf()).
                        flashAttr("client", clientDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
        verify(clientService, times(1)).addNewClient(clientDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editClientFormMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(clientDto);

        //THEN
        mockMvc.perform(get("/clients/edit/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/clientTemplates/editClientForm"))
                .andExpect(model().attribute("client", clientDto))
                .andExpect(model().attribute("oldFirstName", TestConstants.FIRSTNAME))
                .andExpect(model().attribute("oldLastName", TestConstants.LASTNAME))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(clientService, times(1)).getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editClientMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();
        partialClientDto = ClientMocks.mockPartialClientDto();

    //WHEN
        when(clientService.updateClientInfo(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto)).thenReturn(clientDto);

        //THEN
        mockMvc.perform(post("/clients/updateClient/{oldFirstName}/{oldLastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME).with(csrf()).
                        flashAttr("client", partialClientDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
        verify(clientService, times(1)).updateClientInfo(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteClientMockMvcTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        mockMvc.perform(get("/clients/delete/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
        verify(clientService, times(1)).deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }
}
