package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.controller.RegisteredVaccineController;
import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.service.RegisteredVaccineService;
import com.unibuc.main.utils.MedicalRecordMocks;
import com.unibuc.main.utils.RegisteredVaccineMocks;
import com.unibuc.main.utils.VaccineMocks;
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
@WebMvcTest(controllers = RegisteredVaccineController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class RegisteredVaccineControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    RegisteredVaccineService registeredVaccineService;
    @MockBean
    Model model;
    @MockBean
    private VaccineRepository vaccineRepository;
    @MockBean
    private MedicalRecordRepository medicalRecordRepository;
    @MockBean
    private RegisteredVaccineMapper registeredVaccineMapper;
    RegisteredVaccineDto registeredVaccineDto;
    PartialRegisteredVaccineDto partialRegisteredVaccineDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getRegisteredVaccinesPageMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        List<RegisteredVaccineDto> registeredVaccineDtos = new ArrayList<>();
        registeredVaccineDtos.add(registeredVaccineDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(registeredVaccineService.findPaginatedRegisteredVaccines(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(registeredVaccineDtos));

        //THEN
        mockMvc.perform(get("/registeredVaccines?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/registeredVaccinePaginated"))
                .andExpect(model().attribute("registeredVaccinePage", new PageImpl<>(registeredVaccineDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(registeredVaccineService, times(1)).findPaginatedRegisteredVaccines(PageRequest.of(currentPage - 1, pageSize));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getRegisteredVaccineByMedicalRecordIdMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        
        //WHEN
        when(registeredVaccineService.getRegisteredVaccineByMedicalRecordId(1L)).thenReturn(registeredVaccineDto);

        //THEN
        mockMvc.perform(get("/registeredVaccines/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/registeredVaccineDetails"))
                .andExpect(model().attribute("registeredVaccine", registeredVaccineDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(registeredVaccineService, times(1)).getRegisteredVaccineByMedicalRecordId(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addRegisteredVaccineFormMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();
        
        //WHEN
        when(vaccineRepository.findAll()).thenReturn(Collections.singletonList(VaccineMocks.mockVaccine()));
        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(MedicalRecordMocks.mockMedicalRecord()));

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/registeredVaccines/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/addRegisteredVaccineForm"))
                .andExpect(model().attribute("registeredVaccine", new PartialRegisteredVaccineDto()))
                .andExpect(model().attribute("vaccinesAll", Collections.singletonList(VaccineMocks.mockVaccine())))
                .andExpect(model().attribute("medicalRecordsAll", Collections.singletonList(MedicalRecordMocks.mockMedicalRecord())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveRegisteredVaccineMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto)).thenReturn(registeredVaccineDto);

        //THEN
        mockMvc.perform(post("/registeredVaccines").with(csrf()).
                    flashAttr("registeredVaccine", partialRegisteredVaccineDto))
                .andExpect(status().is3xxRedirection())
                
                .andExpect(view().name("redirect:/registeredVaccines"));
        verify(registeredVaccineService, times(1)).associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editRegisteredVaccineFormMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.getRegisteredVaccineByMedicalRecordId(1L)).thenReturn(registeredVaccineDto);
        when(registeredVaccineMapper.mapDtoToPartial(registeredVaccineDto)).thenReturn(partialRegisteredVaccineDto);
        when(vaccineRepository.findAll()).thenReturn(Collections.singletonList(VaccineMocks.mockVaccine()));
        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(MedicalRecordMocks.mockMedicalRecord()));

        //THEN
        mockMvc.perform(get("/registeredVaccines/edit/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/editRegisteredVaccineForm"))
                .andExpect(model().attribute("registeredVaccine", partialRegisteredVaccineDto))
                .andExpect(model().attribute("vaccinesAll", Collections.singletonList(VaccineMocks.mockVaccine())))
                .andExpect(model().attribute("medicalRecordsAll", Collections.singletonList(MedicalRecordMocks.mockMedicalRecord())))
                .andExpect(model().attribute("vaccinesAll", Collections.singletonList(VaccineMocks.mockVaccine())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(registeredVaccineService, times(1)).getRegisteredVaccineByMedicalRecordId(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editRegisteredVaccineMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.updateVaccinesWithMedicalRecord(1L, partialRegisteredVaccineDto)).thenReturn(registeredVaccineDto);

        //THEN
        mockMvc.perform(post("/registeredVaccines/updateRegisteredVaccine/{medicalRecordId}", 1L).with(csrf()).
                        flashAttr("registeredVaccine", partialRegisteredVaccineDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registeredVaccines"));
        verify(registeredVaccineService, times(1)).updateVaccinesWithMedicalRecord(1L, partialRegisteredVaccineDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteRegisteredVaccineMockMvcTest() throws Exception {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.deleteVaccinesFromMedicalRecord(1L)).thenReturn(true);

        //THEN
        mockMvc.perform(get("/registeredVaccines/delete/{medicalRecordId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registeredVaccines"));
        verify(registeredVaccineService, times(1)).deleteVaccinesFromMedicalRecord(1L);
    }
}
