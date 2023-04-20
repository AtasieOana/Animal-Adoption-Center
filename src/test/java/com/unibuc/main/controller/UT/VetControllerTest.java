package com.unibuc.main.controller.UT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.VetController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.VetService;
import com.unibuc.main.utils.EmployeeMocks;
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
public class VetControllerTest {

    @InjectMocks
    VetController vetController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    VetService vetService;
    EmployeeDto vetDto;

    @Test
    public void getVetsPageTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();
        List<EmployeeDto> vetDtos = new ArrayList<>();
        vetDtos.add(vetDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(vetService.findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize), pageSize, currentPage-1)).thenReturn(new PageImpl<>(vetDtos));

        //THEN
        String viewName = vetController.getVetsPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/employeeTemplates/vetPaginated", viewName);
        verify(vetService, times(1)).findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize), pageSize, currentPage-1);

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("vetPage"), argumentCaptor.capture() );

        PageImpl vetDtoArg = argumentCaptor.getValue();
        assertEquals(vetDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getEmployeeByNameTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(vetDto);

        //THEN
        ModelAndView modelAndViewVet = vetController.getVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
        assertEquals("/employeeTemplates/vetDetails", modelAndViewVet.getViewName());
        verify(vetService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
    }

    @Test
    public void addVetFormTest() {
        //GIVEN

        //WHEN

        //THEN
        String viewName = vetController.addVetForm(model);
        assertEquals("/employeeTemplates/addVetForm", viewName);

        ArgumentCaptor<EmployeeDto> argumentCaptor = ArgumentCaptor.forClass(EmployeeDto.class);
        verify(model, times(1))
                .addAttribute(eq("vet"), argumentCaptor.capture() );

        EmployeeDto vetDtoArg = argumentCaptor.getValue();
        assertEquals(vetDtoArg, new EmployeeDto());
    }

    @Test
    public void saveVetTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.addNewEmployee(vetDto)).thenReturn(vetDto);

        //THEN
        String viewName = vetController.saveVet(vetDto, bindingResult);
        assertEquals("redirect:/vets", viewName);
        verify(vetService, times(1)).addNewEmployee(vetDto);
    }

    @Test
    public void editVetFormTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(vetDto);

        //THEN
        String viewName = vetController.editVetForm(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, model);
        assertEquals("/employeeTemplates/editVetForm", viewName);
        verify(vetService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);

        ArgumentCaptor<EmployeeDto> argumentCaptor1 = ArgumentCaptor.forClass(EmployeeDto.class);
        verify(model, times(1))
                .addAttribute(eq("vet"), argumentCaptor1.capture() );
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldLastName"), argumentCaptor2.capture() );
        ArgumentCaptor<String> argumentCaptor3 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldFirstName"), argumentCaptor3.capture() );
        EmployeeDto vetDtoArg = argumentCaptor1.getValue();
        assertEquals(vetDtoArg, vetDto);
        String oldLastNameArg = argumentCaptor2.getValue();
        assertEquals(oldLastNameArg, TestConstants.LASTNAME_VET);
        String oldFirstNameArg = argumentCaptor3.getValue();
        assertEquals(oldFirstNameArg, TestConstants.FIRSTNAME);
    }

    @Test
    public void editVetTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto)).thenReturn(vetDto);

        //THEN
        String viewName = vetController.editVet(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto, bindingResult);
        assertEquals("redirect:/vets", viewName);
        verify(vetService, times(1)).updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto);
    }

    @Test
    public void deleteVetTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(true);

        //THEN
        String viewName = vetController.deleteVet(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
        assertEquals("redirect:/vets", viewName);
        verify(vetService, times(1)).deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
    }
}

