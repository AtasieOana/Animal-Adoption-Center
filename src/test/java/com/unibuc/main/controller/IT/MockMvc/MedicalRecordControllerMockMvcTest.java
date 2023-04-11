package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.MedicalRecordController;
import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.EditMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.MedicalRecordService;
import com.unibuc.main.utils.AnimalMocks;
import com.unibuc.main.utils.EmployeeMocks;
import com.unibuc.main.utils.MedicalRecordMocks;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class MedicalRecordControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    MedicalRecordService medicalRecordService;
    @MockBean
    Model model;
    @MockBean
    private AnimalRepository animalRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    MedicalRecordDto medicalRecordDto;
    AddMedicalRecordDto addMedicalRecordDto;
    EditMedicalRecordDto editMedicalRecordDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getMedicalRecordsPageMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(medicalRecordService.findPaginatedMedicalRecords(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(medicalRecordDtos));

        //THEN
        mockMvc.perform(get("/medicalRecords?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/medicalRecordPaginated"))
                .andExpect(model().attribute("medicalRecordPage", new PageImpl<>(medicalRecordDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(medicalRecordService, times(1)).findPaginatedMedicalRecords(PageRequest.of(currentPage - 1, pageSize));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getMedicalRecordByIdMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(medicalRecordDto);

        //THEN
        mockMvc.perform(get("/medicalRecords/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/medicalRecordDetails"))
                .andExpect(model().attribute("medicalRecord", medicalRecordDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(medicalRecordService, times(1)).getMedicalRecordById(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addMedicalRecordFormMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(animalRepository.findAllByClientIsNull()).thenReturn(Collections.singletonList(AnimalMocks.mockAnimal()));
        when(employeeRepository.findAllByExperienceNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockVet()));

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/medicalRecords/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/addMedicalRecordForm"))
                .andExpect(model().attribute("medicalRecord", new AddMedicalRecordDto()))
                .andExpect(model().attribute("animalsAll", Collections.singletonList(AnimalMocks.mockAnimal())))
                .andExpect(model().attribute("vetsAll", Collections.singletonList(EmployeeMocks.mockVet())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveMedicalRecordMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(medicalRecordService.addMedicalRecord(addMedicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        mockMvc.perform(post("/medicalRecords").with(csrf()).
                    flashAttr("medicalRecord", addMedicalRecordDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicalRecords"));
        verify(medicalRecordService, times(1)).addMedicalRecord(addMedicalRecordDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editMedicalRecordFormMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(medicalRecordDto);
        when(animalRepository.findAllByClientIsNull()).thenReturn(Collections.singletonList(AnimalMocks.mockAnimal()));
        when(employeeRepository.findAllByExperienceNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockVet()));

        //THEN
        mockMvc.perform(get("/medicalRecords/edit/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/editMedicalRecordForm"))
                .andExpect(model().attribute("medicalRecord", medicalRecordDto))
                .andExpect(model().attribute("animalsAll", Collections.singletonList(AnimalMocks.mockAnimal())))
                .andExpect(model().attribute("vetsAll", Collections.singletonList(EmployeeMocks.mockVet())))
                .andExpect(model().attribute("medicalRecordId", 1L))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(medicalRecordService, times(1)).getMedicalRecordById(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editMedicalRecordMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        editMedicalRecordDto = MedicalRecordMocks.mockEditMedicalRecordDto();

        //WHEN
        when(medicalRecordService.updateMedicalRecord(medicalRecordDto.getId(), editMedicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        mockMvc.perform(post("/medicalRecords/updateMedicalRecord/{medicalRecordId}", 1L).with(csrf()).
                        flashAttr("medicalRecord", editMedicalRecordDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicalRecords"));
        verify(medicalRecordService, times(1)).updateMedicalRecord(1L, editMedicalRecordDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteMedicalRecordMockMvcTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.deleteMedicalRecord(medicalRecordDto.getId())).thenReturn(true);

        //THEN
        mockMvc.perform(get("/medicalRecords/delete/{medicalRecordId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicalRecords"));
        verify(medicalRecordService, times(1)).deleteMedicalRecord(1L);
    }
}
