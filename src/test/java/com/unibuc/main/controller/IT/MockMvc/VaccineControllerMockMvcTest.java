package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.VaccineController;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = VaccineController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class VaccineControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    VaccineService vaccineService;
    @MockBean
    Model model;
    VaccineDto vaccineDto;
    PartialVaccineDto partialVaccineDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVaccinesPageMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();
        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(vaccineService.findPaginatedVaccines(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(vaccineDtos));

        //THEN
        mockMvc.perform(get("/vaccines?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/vaccinePaginated"))
                .andExpect(model().attribute("vaccinePage", new PageImpl<>(vaccineDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(vaccineService, times(1)).findPaginatedVaccines(PageRequest.of(currentPage - 1, pageSize));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVaccineByNameMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.getVaccineByName(TestConstants.VACCINE_NAME)).thenReturn(vaccineDto);

        //THEN
        mockMvc.perform(get("/vaccines/{vaccineType}", TestConstants.VACCINE_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/vaccineDetails"))
                .andExpect(model().attribute("vaccine", vaccineDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(vaccineService, times(1)).getVaccineByName(TestConstants.VACCINE_NAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addVaccineFormMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/vaccines/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/addVaccineForm"))
                .andExpect(model().attribute("vaccine", new VaccineDto()))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveVaccineMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.addVaccine(vaccineDto)).thenReturn(vaccineDto);

        //THEN
        mockMvc.perform(post("/vaccines").with(csrf()).
                    flashAttr("vaccine", vaccineDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vaccines"));
        verify(vaccineService, times(1)).addVaccine(vaccineDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVaccineFormMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.getVaccineByName(TestConstants.VACCINE_NAME)).thenReturn(vaccineDto);

        //THEN
        mockMvc.perform(get("/vaccines/edit/{oldVaccineName}", TestConstants.VACCINE_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/editVaccineForm"))
                .andExpect(model().attribute("vaccine", vaccineDto))
                .andExpect(model().attribute("oldVaccineName", TestConstants.VACCINE_NAME))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(vaccineService, times(1)).getVaccineByName(TestConstants.VACCINE_NAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVaccineMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();
        partialVaccineDto = VaccineMocks.mockPartialVaccineDto();

        //WHEN
        when(vaccineService.updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto)).thenReturn(vaccineDto);

        //THEN
        mockMvc.perform(post("/vaccines/updateVaccine/{oldVaccineName}", TestConstants.VACCINE_NAME).with(csrf()).
                        flashAttr("vaccine", partialVaccineDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vaccines"));
        verify(vaccineService, times(1)).updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteExpiredVaccinesMockMvcTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.deleteExpiredVaccines()).thenReturn(ProjectConstants.DELETED_EXP_VACCINES);

        //THEN
        mockMvc.perform(get("/vaccines/deleteExpiredVaccines"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("result", ProjectConstants.DELETED_EXP_VACCINES))
                .andExpect(view().name("redirect:/vaccines"));
        verify(vaccineService, times(1)).deleteExpiredVaccines();
    }
}
