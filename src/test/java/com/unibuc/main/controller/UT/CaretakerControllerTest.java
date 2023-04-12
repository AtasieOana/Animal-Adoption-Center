package com.unibuc.main.controller.UT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.CaretakerController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.CaretakerService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaretakerControllerTest {

    @InjectMocks
    CaretakerController caretakerController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    CaretakerService caretakerService;
    EmployeeDto caretakerDto;

    @Test
    public void getCaretakersPageTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        List<EmployeeDto> caretakerDtos = new ArrayList<>();
        caretakerDtos.add(caretakerDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(caretakerService.findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(caretakerDtos));

        //THEN
        String viewName = caretakerController.getCaretakersPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/employeeTemplates/caretakerPaginated", viewName);
        verify(caretakerService, times(1)).findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("caretakerPage"), argumentCaptor.capture() );

        PageImpl caretakerDtoArg = argumentCaptor.getValue();
        assertEquals(caretakerDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getEmployeeByNameTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(caretakerDto);

        //THEN
        ModelAndView modelAndViewCaretaker = caretakerController.getCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals("/employeeTemplates/caretakerDetails", modelAndViewCaretaker.getViewName());
        verify(caretakerService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }

    @Test
    public void addCaretakerFormTest() {
        //GIVEN

        //WHEN

        //THEN
        String viewName = caretakerController.addCaretakerForm(model);
        assertEquals("/employeeTemplates/addCaretakerForm", viewName);

        ArgumentCaptor<EmployeeDto> argumentCaptor = ArgumentCaptor.forClass(EmployeeDto.class);
        verify(model, times(1))
                .addAttribute(eq("caretaker"), argumentCaptor.capture() );

        EmployeeDto caretakerDtoArg = argumentCaptor.getValue();
        assertEquals(caretakerDtoArg, new EmployeeDto());
    }

    @Test
    public void saveCaretakerTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.addNewEmployee(caretakerDto)).thenReturn(caretakerDto);

        //THEN
        String viewName = caretakerController.saveCaretaker(caretakerDto, bindingResult);
        assertEquals("redirect:/caretakers", viewName);
        verify(caretakerService, times(1)).addNewEmployee(caretakerDto);
    }

    @Test
    public void editCaretakerFormTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(caretakerDto);

        //THEN
        String viewName = caretakerController.editCaretakerForm(TestConstants.FIRSTNAME, TestConstants.LASTNAME, model);
        assertEquals("/employeeTemplates/editCaretakerForm", viewName);
        verify(caretakerService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);

        ArgumentCaptor<EmployeeDto> argumentCaptor1 = ArgumentCaptor.forClass(EmployeeDto.class);
        verify(model, times(1))
                .addAttribute(eq("caretaker"), argumentCaptor1.capture() );
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldLastName"), argumentCaptor2.capture() );
        ArgumentCaptor<String> argumentCaptor3 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldFirstName"), argumentCaptor3.capture() );
        EmployeeDto caretakerDtoArg = argumentCaptor1.getValue();
        assertEquals(caretakerDtoArg, caretakerDto);
        String oldLastNameArg = argumentCaptor2.getValue();
        assertEquals(oldLastNameArg, TestConstants.LASTNAME);
        String oldFirstNameArg = argumentCaptor3.getValue();
        assertEquals(oldFirstNameArg, TestConstants.FIRSTNAME);
    }

    @Test
    public void editCaretakerTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto)).thenReturn(caretakerDto);

        //THEN
        String viewName = caretakerController.editCaretaker(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto, bindingResult);
        assertEquals("redirect:/caretakers", viewName);
        verify(caretakerService, times(1)).updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto);
    }

    @Test
    public void deleteCaretakerTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        String viewName = caretakerController.deleteCaretaker(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals("redirect:/caretakers", viewName);
        verify(caretakerService, times(1)).deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }
}

