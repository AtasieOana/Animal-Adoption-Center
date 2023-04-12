package com.unibuc.main.controller.UT;

import com.unibuc.main.controller.CageController;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.CageService;
import com.unibuc.main.utils.CageMocks;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CageControllerTest {

    @InjectMocks
    CageController cageController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    CageService cageService;
    @Mock
    EmployeeRepository employeeRepository;
    
    CageDto cageDto;
    PartialCageDto partialCageDto;
    
    @Test
    public void getCagesPageTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(cageService.findPaginatedCages(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(cageDtos));

        //THEN
        String viewName = cageController.getCagesPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/cageTemplates/cagePaginated", viewName);
        verify(cageService, times(1)).findPaginatedCages(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("cagePage"), argumentCaptor.capture() );

        PageImpl cageDtoArg = argumentCaptor.getValue();
        assertEquals(cageDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getCageByIdTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageService.getCageById(1L)).thenReturn(cageDto);

        //THEN
        ModelAndView modelAndViewCage = cageController.getCageById(1L);
        assertEquals("/cageTemplates/cageDetails", modelAndViewCage.getViewName());
        verify(cageService, times(1)).getCageById(1L);
    }

    @Test
    public void addCageFormTest() {
        //GIVEN

        //WHEN
        when(employeeRepository.findAllByResponsibilityNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockCaretaker()));

        //THEN
        String viewName = cageController.addCageForm(model);
        assertEquals("/cageTemplates/addCageForm", viewName);
        verify(employeeRepository, times(1)).findAllByResponsibilityNotNull();

        ArgumentCaptor<PartialCageDto> argumentCaptor = ArgumentCaptor.forClass(PartialCageDto.class);
        verify(model, times(1))
                .addAttribute(eq("cage"), argumentCaptor.capture() );
        ArgumentCaptor<List<Employee>> argumentCaptorEmp = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("caretakersAll"), argumentCaptorEmp.capture() );

        PartialCageDto cageDtoArg = argumentCaptor.getValue();
        assertEquals(cageDtoArg, new PartialCageDto());
        List<Employee> caretakersAllArg = argumentCaptorEmp.getValue();
        assertEquals(caretakersAllArg, Collections.singletonList(EmployeeMocks.mockCaretaker()));
    }

    @Test
    public void saveCageTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.addCage(partialCageDto)).thenReturn(cageDto);

        //THEN
        String viewName = cageController.saveCage(partialCageDto, bindingResult, model);
        assertEquals("redirect:/cages", viewName);
        verify(cageService, times(1)).addCage(partialCageDto);
    }

    @Test
    public void editCageFormTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.getPartialCageById(1L)).thenReturn(partialCageDto);
        when(employeeRepository.findAllByResponsibilityNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockCaretaker()));

        //THEN
        String viewName = cageController.editCageForm(1L, model);
        assertEquals("/cageTemplates/editCageForm", viewName);
        verify(cageService, times(1)).getPartialCageById(1L);
        verify(employeeRepository, times(1)).findAllByResponsibilityNotNull();

        ArgumentCaptor<PartialCageDto> argumentCaptor = ArgumentCaptor.forClass(PartialCageDto.class);
        verify(model, times(1))
                .addAttribute(eq("cage"), argumentCaptor.capture() );
        ArgumentCaptor<List<Employee>> argumentCaptorEmp = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("caretakersAll"), argumentCaptorEmp.capture() );

        PartialCageDto cageDtoArg = argumentCaptor.getValue();
        assertEquals(cageDtoArg, partialCageDto);
        List<Employee> caretakersAllArg = argumentCaptorEmp.getValue();
        assertEquals(caretakersAllArg, Collections.singletonList(EmployeeMocks.mockCaretaker()));
    }

    @Test
    public void editCageTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.updateCage(1L, partialCageDto)).thenReturn(cageDto);

        //THEN
        String viewName = cageController.editCage(1L, partialCageDto, bindingResult, model);
        assertEquals("redirect:/cages", viewName);
        verify(cageService, times(1)).updateCage(1L, partialCageDto);
    }

    @Test
    public void deleteCageTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageService.deleteCage(1L)).thenReturn(true);

        //THEN
        String viewName = cageController.deleteCage(1L);
        assertEquals("redirect:/cages", viewName);
        verify(cageService, times(1)).deleteCage(1L);
    }
}

