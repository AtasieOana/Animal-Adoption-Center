package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.entity.Client;
import com.unibuc.main.entity.PersonDetails;
import com.unibuc.main.exception.ClientAlreadyExistsException;
import com.unibuc.main.exception.ClientNotFoundException;
import com.unibuc.main.mapper.ClientMapper;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    ClientMapper clientMapper;

    Client client;

    ClientDto clientDto;
    PartialClientDto partialClientDto;

    @Test
    public void testGetAllClients() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();

        List<Client> clientList = new ArrayList<>();
        clientList.add(client);
        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);

        //WHEN
        when(clientRepository.findAll()).thenReturn(clientList);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        //THEN
        List<ClientDto> result = clientService.getAllClients();
        assertEquals(result, clientDtos);
    }

    @Test
    public void testGetClientByName() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        //THEN
        ClientDto result = clientService.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result, clientDto);
        assertNotNull(result);
    }

    @Test
    public void testGetAvgAgeForClient() {
        //GIVEN
        client = null;
        clientDto = null;

        //WHEN
        when(clientRepository.avgAge()).thenReturn(21.0);

        //THEN
        Integer result = clientService.getAvgAgeForClient();
        assertEquals(result, 21);
    }

    @Test
    public void testGetAvgAgeForClientZero() {
        //GIVEN
        client = null;
        clientDto = null;

        //WHEN
        when(clientRepository.avgAge()).thenReturn(null);

        //THEN
        Integer result = clientService.getAvgAgeForClient();
        assertEquals(result, 0);
    }


    @Test
    public void testGetClientByNameException() {
        //GIVEN
        client = null;
        clientDto = null;

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));

        //THEN
        ClientNotFoundException clientNotFoundException = assertThrows(ClientNotFoundException.class, () -> clientService.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.CLIENT_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), clientNotFoundException.getMessage());
    }

    @Test
    public void testAddNewClient() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.empty());
        when(clientMapper.mapToClient(clientDto)).thenReturn(client);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);
        when(clientRepository.save(client)).thenReturn(client);

        //THEN
        ClientDto result = clientService.addNewClient(clientDto);
        assertEquals(result, clientDto);
        assertThat(result.getFirstName()).isNotNull();
        assertThat(result.getLastName()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewClientException() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));

        //THEN
        ClientAlreadyExistsException clientAlreadyExistsException = assertThrows(ClientAlreadyExistsException.class, () -> clientService.addNewClient(clientDto));
        assertEquals(String.format(ProjectConstants.CLIENT_EXISTS, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), clientAlreadyExistsException.getMessage());
    }


    @Test
    public void testDeleteClient() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));

        //THEN
        Boolean result = clientService.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result, true);
    }

    @Test
    public void testDeleteClientException() {
        //GIVEN
        client = null;
        clientDto = null;

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));

        //THEN
        ClientNotFoundException clientNotFoundException = assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.CLIENT_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), clientNotFoundException.getMessage());
    }


    @Test
    public void testUpdateClientInfo() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();
        partialClientDto = ClientMocks.mockPartialClientDto();
        partialClientDto.setPhoneNumber("0764112345");

        PersonDetails personDetails =  client.getPersonDetails();
        personDetails.setPhoneNumber("0764112345");
        Client updatedClient = ClientMocks.mockClient();
        updatedClient.setPersonDetails(personDetails);
        clientDto.setPhoneNumber("0764112345");

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));
        when(clientMapper.mapToClientDto(updatedClient)).thenReturn(clientDto);
        when(clientRepository.save(updatedClient)).thenReturn(updatedClient);

        //THEN
        ClientDto result = clientService.updateClientInfo(TestConstants.FIRSTNAME,TestConstants.LASTNAME, partialClientDto);
        assertEquals(result, clientDto);
        assertEquals(result.getFirstName(), TestConstants.FIRSTNAME);
        assertEquals(result.getPhoneNumber(), partialClientDto.getPhoneNumber());
        assertNotNull(result);
    }

    @Test
    public void testUpdateClientException() {
        //GIVEN
        client = null;
        clientDto = null;

        //WHEN
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(client));

        //THEN
        ClientNotFoundException clientNotFoundException = assertThrows(ClientNotFoundException.class, () -> clientService.updateClientInfo(TestConstants.FIRSTNAME,TestConstants.LASTNAME, partialClientDto));
        assertEquals(String.format(ProjectConstants.CLIENT_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), clientNotFoundException.getMessage());
    }

    @Test
    public void testFindPaginatedDiets() {
        //GIVEN
        client = ClientMocks.mockClient();
        clientDto = ClientMocks.mockClientDto();
        Pageable pageable = PageRequest.of(0,20);

        List<Client> clientList = new ArrayList<>();
        clientList.add(client);
        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);

        //WHEN
        when(clientRepository.findAll(pageable)).thenReturn(new PageImpl<>(clientList));
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        //THEN
        Page<ClientDto> result = clientService.findPaginatedClients(pageable);
        assertEquals(result, new PageImpl<>(clientDtos));
    }
}
