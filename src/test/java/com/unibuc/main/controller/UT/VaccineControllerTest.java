package com.unibuc.main.controller.UT;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.VaccineController;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
import com.unibuc.main.utils.VaccineMocks;
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
public class VaccineControllerTest {

    @InjectMocks
    VaccineController vaccineController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    VaccineService vaccineService;
    @Mock
    RedirectAttributes redirectAttributes;
    VaccineDto vaccineDto;
    PartialVaccineDto partialVaccineDto;

    @Test
    public void getVaccinesPageTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();
        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(vaccineService.findPaginatedVaccines(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(vaccineDtos));

        //THEN
        String viewName = vaccineController.getVaccinesPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/vaccineTemplates/vaccinePaginated", viewName);
        verify(vaccineService, times(1)).findPaginatedVaccines(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("vaccinePage"), argumentCaptor.capture() );

        PageImpl vaccineDtoArg = argumentCaptor.getValue();
        assertEquals(vaccineDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getVaccineByNameTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.getVaccineByName(TestConstants.VACCINE_NAME)).thenReturn(vaccineDto);

        //THEN
        ModelAndView modelAndViewVaccine = vaccineController.getVaccineByName(TestConstants.VACCINE_NAME);
        assertEquals("/vaccineTemplates/vaccineDetails", modelAndViewVaccine.getViewName());
        verify(vaccineService, times(1)).getVaccineByName(TestConstants.VACCINE_NAME);
    }

    @Test
    public void addVaccineFormTest() {
        //GIVEN

        //WHEN

        //THEN
        String viewName = vaccineController.addVaccineForm(model);
        assertEquals("/vaccineTemplates/addVaccineForm", viewName);

        ArgumentCaptor<VaccineDto> argumentCaptor = ArgumentCaptor.forClass(VaccineDto.class);
        verify(model, times(1))
                .addAttribute(eq("vaccine"), argumentCaptor.capture() );

        VaccineDto vaccineDtoArg = argumentCaptor.getValue();
        assertEquals(vaccineDtoArg, new VaccineDto());
    }

    @Test
    public void saveVaccineTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.addVaccine(vaccineDto)).thenReturn(vaccineDto);

        //THEN
        String viewName = vaccineController.saveVaccine(vaccineDto, bindingResult);
        assertEquals("redirect:/vaccines", viewName);
        verify(vaccineService, times(1)).addVaccine(vaccineDto);
    }

    @Test
    public void editVaccineFormTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.getVaccineByName(TestConstants.VACCINE_NAME)).thenReturn(vaccineDto);

        //THEN
        String viewName = vaccineController.editVaccineForm(TestConstants.VACCINE_NAME, model);
        assertEquals("/vaccineTemplates/editVaccineForm", viewName);
        verify(vaccineService, times(1)).getVaccineByName(TestConstants.VACCINE_NAME);

        ArgumentCaptor<VaccineDto> argumentCaptor1 = ArgumentCaptor.forClass(VaccineDto.class);
        verify(model, times(1))
                .addAttribute(eq("vaccine"), argumentCaptor1.capture() );
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldVaccineName"), argumentCaptor2.capture() );

        VaccineDto vaccineDtoArg = argumentCaptor1.getValue();
        assertEquals(vaccineDtoArg, vaccineDto);
        String oldVaccineTypeArg = argumentCaptor2.getValue();
        assertEquals(oldVaccineTypeArg, TestConstants.VACCINE_NAME);
    }

    @Test
    public void editVaccineTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();
        partialVaccineDto = VaccineMocks.mockPartialVaccineDto();

        //WHEN
        when(vaccineService.updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto)).thenReturn(vaccineDto);

        //THEN
        String viewName = vaccineController.editVaccine(TestConstants.VACCINE_NAME, partialVaccineDto, bindingResult);
        assertEquals("redirect:/vaccines", viewName);
        verify(vaccineService, times(1)).updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto);
    }

    @Test
    public void deleteVaccineTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.deleteExpiredVaccines()).thenReturn(ProjectConstants.DELETED_EXP_VACCINES);

        //THEN
        String viewName = vaccineController.deleteExpiredVaccines(redirectAttributes);
        assertEquals("redirect:/vaccines", viewName);
        verify(vaccineService, times(1)).deleteExpiredVaccines();

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1))
                .addAttribute(eq("result"), argumentCaptor.capture() );
        String resultArg = argumentCaptor.getValue();
        assertEquals(resultArg, ProjectConstants.DELETED_EXP_VACCINES);
    }
}

