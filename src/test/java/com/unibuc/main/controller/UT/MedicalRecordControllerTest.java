package com.unibuc.main.controller.UT;

import com.unibuc.main.controller.MedicalRecordController;
import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.EditMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.MedicalRecordService;
import com.unibuc.main.utils.AnimalMocks;
import com.unibuc.main.utils.EmployeeMocks;
import com.unibuc.main.utils.MedicalRecordMocks;
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
public class MedicalRecordControllerTest {

    @InjectMocks
    MedicalRecordController medicalRecordController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    MedicalRecordService medicalRecordService;
    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    MedicalRecordDto medicalRecordDto;
    AddMedicalRecordDto addMedicalRecordDto;
    EditMedicalRecordDto editMedicalRecordDto;
    
    @Test
    public void getMedicalRecordsPageTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(medicalRecordService.findPaginatedMedicalRecords(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(medicalRecordDtos));

        //THEN
        String viewName = medicalRecordController.getMedicalRecordsPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/medicalRecordTemplates/medicalRecordPaginated", viewName);
        verify(medicalRecordService, times(1)).findPaginatedMedicalRecords(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("medicalRecordPage"), argumentCaptor.capture() );

        PageImpl medicalRecordDtoArg = argumentCaptor.getValue();
        assertEquals(medicalRecordDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getMedicalRecordByIdTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(medicalRecordDto);

        //THEN
        ModelAndView modelAndViewMedicalRecord = medicalRecordController.getMedicalRecordById(1L);
        assertEquals("/medicalRecordTemplates/medicalRecordDetails", modelAndViewMedicalRecord.getViewName());
        verify(medicalRecordService, times(1)).getMedicalRecordById(1L);
    }

    @Test
    public void addMedicalRecordFormTest() {
        //GIVEN
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(animalRepository.findAllByClientIsNull()).thenReturn(Collections.singletonList(AnimalMocks.mockAnimal()));
        when(employeeRepository.findAllByExperienceNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockVet()));

        //THEN
        String viewName = medicalRecordController.addMedicalRecordForm(model);
        assertEquals("/medicalRecordTemplates/addMedicalRecordForm", viewName);
        verify(employeeRepository, times(1)).findAllByExperienceNotNull();
        verify(animalRepository, times(1)).findAllByClientIsNull();

        ArgumentCaptor<AddMedicalRecordDto> argumentCaptor = ArgumentCaptor.forClass(AddMedicalRecordDto.class);
        verify(model, times(1))
                .addAttribute(eq("medicalRecord"), argumentCaptor.capture() );
        ArgumentCaptor<List<Employee>> argumentCaptorEmp = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("vetsAll"), argumentCaptorEmp.capture() );
        ArgumentCaptor<List<Animal>> argumentCaptorAn = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("animalsAll"), argumentCaptorAn.capture() );
        
        AddMedicalRecordDto medicalRecordDtoArg = argumentCaptor.getValue();
        assertEquals(medicalRecordDtoArg, new AddMedicalRecordDto());
        List<Employee> vetsAllArg = argumentCaptorEmp.getValue();
        assertEquals(vetsAllArg, Collections.singletonList(EmployeeMocks.mockVet()));
        List<Animal> animalsAllArg = argumentCaptorAn.getValue();
        assertEquals(animalsAllArg, Collections.singletonList(AnimalMocks.mockAnimal()));
    }

    @Test
    public void saveMedicalRecordTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(medicalRecordService.addMedicalRecord(addMedicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        String viewName = medicalRecordController.saveMedicalRecord(addMedicalRecordDto, bindingResult, model);
        assertEquals("redirect:/medicalRecords", viewName);
        verify(medicalRecordService, times(1)).addMedicalRecord(addMedicalRecordDto);
    }

    @Test
    public void editMedicalRecordFormTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(medicalRecordDto);
        when(animalRepository.findAllByClientIsNull()).thenReturn(Collections.singletonList(AnimalMocks.mockAnimal()));
        when(employeeRepository.findAllByExperienceNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockVet()));

        //THEN
        String viewName = medicalRecordController.editMedicalRecordForm(1L, model);
        assertEquals("/medicalRecordTemplates/editMedicalRecordForm", viewName);
        verify(medicalRecordService, times(1)).getMedicalRecordById(1L);
        verify(employeeRepository, times(1)).findAllByExperienceNotNull();
        verify(animalRepository, times(1)).findAllByClientIsNull();

        ArgumentCaptor<MedicalRecordDto> argumentCaptor = ArgumentCaptor.forClass(MedicalRecordDto.class);
        verify(model, times(1))
                .addAttribute(eq("medicalRecord"), argumentCaptor.capture() );
        ArgumentCaptor<List<Employee>> argumentCaptorEmp = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("vetsAll"), argumentCaptorEmp.capture() );
        ArgumentCaptor<List<Animal>> argumentCaptorAn = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("animalsAll"), argumentCaptorAn.capture() );

        MedicalRecordDto medicalRecordDtoArg = argumentCaptor.getValue();
        assertEquals(medicalRecordDtoArg, medicalRecordDto);
        List<Employee> vetsAllArg = argumentCaptorEmp.getValue();
        assertEquals(vetsAllArg, Collections.singletonList(EmployeeMocks.mockVet()));
        List<Animal> animalsAllArg = argumentCaptorAn.getValue();
        assertEquals(animalsAllArg, Collections.singletonList(AnimalMocks.mockAnimal()));
    }

    @Test
    public void editMedicalRecordTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        editMedicalRecordDto = MedicalRecordMocks.mockEditMedicalRecordDto();

        //WHEN
        when(medicalRecordService.updateMedicalRecord(1L, editMedicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        String viewName = medicalRecordController.editMedicalRecord(1L, editMedicalRecordDto, bindingResult, model);
        assertEquals("redirect:/medicalRecords", viewName);
        verify(medicalRecordService, times(1)).updateMedicalRecord(1L, editMedicalRecordDto);
    }

    @Test
    public void deleteMedicalRecordTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.deleteMedicalRecord(1L)).thenReturn(true);

        //THEN
        String viewName = medicalRecordController.deleteMedicalRecord(1L);
        assertEquals("redirect:/medicalRecords", viewName);
        verify(medicalRecordService, times(1)).deleteMedicalRecord(1L);
    }
}

