package com.unibuc.main.controller.UT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.ClientController;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.service.ClientService;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @InjectMocks
    ClientController clientController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    ClientService clientService;
    ClientDto clientDto;
    PartialClientDto partialClientDto;

    @Test
    public void getClientsPageTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();
        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(clientService.findPaginatedClients(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(clientDtos));

        //THEN
        String viewName = clientController.getClientsPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/clientTemplates/clientPaginated", viewName);
        verify(clientService, times(1)).findPaginatedClients(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("clientPage"), argumentCaptor.capture() );

        PageImpl clientDtoArg = argumentCaptor.getValue();
        assertEquals(clientDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getClientByNameTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(clientDto);

        //THEN
        ModelAndView modelAndViewClient = clientController.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals("/clientTemplates/clientDetails", modelAndViewClient.getViewName());
        verify(clientService, times(1)).getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }

    @Test
    public void addClientFormTest() {
        //GIVEN

        //WHEN

        //THEN
        String viewName = clientController.addClientForm(model);
        assertEquals("/clientTemplates/addClientForm", viewName);

        ArgumentCaptor<ClientDto> argumentCaptor = ArgumentCaptor.forClass(ClientDto.class);
        verify(model, times(1))
                .addAttribute(eq("client"), argumentCaptor.capture() );

        ClientDto clientDtoArg = argumentCaptor.getValue();
        assertEquals(clientDtoArg, new ClientDto());
    }

    @Test
    public void saveClientTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.addNewClient(clientDto)).thenReturn(clientDto);

        //THEN
        String viewName = clientController.saveClient(clientDto, bindingResult);
        assertEquals("redirect:/clients", viewName);
        verify(clientService, times(1)).addNewClient(clientDto);
    }

    @Test
    public void editClientFormTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(clientDto);

        //THEN
        String viewName = clientController.editClientForm(TestConstants.FIRSTNAME, TestConstants.LASTNAME, model);
        assertEquals("/clientTemplates/editClientForm", viewName);
        verify(clientService, times(1)).getClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);

        ArgumentCaptor<ClientDto> argumentCaptor1 = ArgumentCaptor.forClass(ClientDto.class);
        verify(model, times(1))
                .addAttribute(eq("client"), argumentCaptor1.capture() );
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldLastName"), argumentCaptor2.capture() );
        ArgumentCaptor<String> argumentCaptor3 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldFirstName"), argumentCaptor3.capture() );
        ClientDto clientDtoArg = argumentCaptor1.getValue();
        assertEquals(clientDtoArg, clientDto);
        String oldLastNameArg = argumentCaptor2.getValue();
        assertEquals(oldLastNameArg, TestConstants.LASTNAME);
        String oldFirstNameArg = argumentCaptor3.getValue();
        assertEquals(oldFirstNameArg, TestConstants.FIRSTNAME);
    }

    @Test
    public void editClientTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();
        partialClientDto = ClientMocks.mockPartialClientDto();

        //WHEN
        when(clientService.updateClientInfo(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto)).thenReturn(clientDto);

        //THEN
        String viewName = clientController.editClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto, bindingResult);
        assertEquals("redirect:/clients", viewName);
        verify(clientService, times(1)).updateClientInfo(TestConstants.FIRSTNAME, TestConstants.LASTNAME, partialClientDto);
    }

    @Test
    public void deleteClientTest() {
        //GIVEN
        clientDto = ClientMocks.mockClientDto();

        //WHEN
        when(clientService.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        String viewName = clientController.deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals("redirect:/clients", viewName);
        verify(clientService, times(1)).deleteClient(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }
}

