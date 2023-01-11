package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.service.ClientService;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {
    @InjectMocks
    ClientController clientController;
    @Mock
    ClientService clientService;
    ClientDto clientDto;
    PartialClientDto partialClientDto;

    @Test
    public void getAllClientsTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);

        //WHEN
        when(clientService.getAllClients()).thenReturn(clientDtos);

        //THEN
        ResponseEntity<List<ClientDto>> result = clientController.getAllClients();
        assertEquals(result.getBody(), clientDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getClientByNameTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.getClientByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME)).thenReturn(clientDto);

        //THEN
        ResponseEntity<ClientDto> result = clientController.getClientByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME);
        assertEquals(result.getBody(), clientDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void avgAgeForClientTest() {
        //GIVEN
        clientDto = null;

        //WHEN
        when(clientService.getAvgAgeForClient()).thenReturn(21);

        //THEN
        ResponseEntity<Integer> result = clientController.getAvgAgeForClient();
        assertEquals(result.getBody(), 21);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void clientsNumberByGenderTest() {
        //GIVEN
        clientDto = null;

        //WHEN
        when(clientService.getClientNumberByGender()).thenReturn(String.format(ProjectConstants.CLIENT_GENDERS, 12, 8, 4));

        //THEN
        ResponseEntity<String> result = clientController.getClientNumberByGender();
        assertEquals(result.getBody(), String.format(ProjectConstants.CLIENT_GENDERS, 12, 8, 4));
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewClientTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.addNewClient(clientDto)).thenReturn(clientDto);

        //THEN
        ResponseEntity<ClientDto> result = clientController.addNewClient(clientDto);
        assertEquals(result.getBody(), clientDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteClientTest() {
        //GIVEN
        clientDto = null;

        //WHEN
        when( clientService.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        ResponseEntity<String> result = clientController.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result.getBody(), ProjectConstants.OBJ_DELETED);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateClientTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();
        ClientDto updatedClient = ClientMocks.mockClientDto();
        updatedClient.setPhoneNumber("0764112345");
        partialClientDto = ClientMocks.mockPartialClientDto();
        partialClientDto.setPhoneNumber("0764112345");

        //WHEN
        when(clientService.updateClientInfo(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto)).thenReturn(updatedClient);

        //THEN
        ResponseEntity<ClientDto> result = clientController.updateClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto);
        assertEquals(result.getBody(), updatedClient);
        assertEquals(Objects.requireNonNull(result.getBody()).getPhoneNumber(), updatedClient.getPhoneNumber());
        assertEquals(result.getStatusCode().value(), 200);
    }
}
