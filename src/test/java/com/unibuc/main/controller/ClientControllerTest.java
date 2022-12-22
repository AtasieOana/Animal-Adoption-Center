package com.unibuc.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.service.ClientService;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    ClientService clientService;
    ClientDto clientDto;
    PartialClientDto partialClientDto;

    @Test
    public void getAllClientsTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);

        //WHEN
        when(clientService.getAllClients()).thenReturn(clientDtos);

        //THEN
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(clientDtos)));
    }

    @Test
    public void getClientByNameTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.getClientByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME)).thenReturn(clientDto);

        //THEN
        mockMvc.perform(get("/clients/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(clientDto)));
    }

    @Test
    public void avgAgeForClientTest() throws Exception {
        //GIVEN
        clientDto = null;

        //WHEN
        when(clientService.getAvgAgeForClient()).thenReturn(21);

        //THEN
        mockMvc.perform(get("/clients/avgAgeForClient"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(21)));
    }

    @Test
    public void clientsNumberByGenderTest() throws Exception {
        //GIVEN
        clientDto = null;

        //WHEN
        when(clientService.getClientNumberByGender()).thenReturn(String.format(ProjectConstants.CLIENT_GENDERS, 12, 8, 4));

        //THEN
        mockMvc.perform(get("/clients/clientsNumberByGender"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(ProjectConstants.CLIENT_GENDERS, 12, 8, 4)));
    }

    @Test
    public void addNewClientTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.addNewClient(clientDto)).thenReturn(clientDto);

        //THEN
        mockMvc.perform(post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(clientDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(clientDto.getLastName()));
    }

    @Test
    public void deleteClientTest() throws Exception {
        //GIVEN
        clientDto = null;

        //WHEN
        when( clientService.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        mockMvc.perform(delete("/clients/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.OBJ_DELETED));
    }

    @Test
    public void updateClientTest() throws Exception {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();
        ClientDto updatedClient = clientDto;
        updatedClient.setPhoneNumber("0764112345");
        partialClientDto = ClientMocks.mockPartialClientDto();
        partialClientDto.setPhoneNumber("0764112345");

        //WHEN
        when(clientService.updateClientInfo(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto)).thenReturn(updatedClient);

        //THEN
        mockMvc.perform(put("/clients/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(partialClientDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(jsonPath("$.firstName").value(clientDto.getFirstName()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedClient.getPhoneNumber()));
    }
}
