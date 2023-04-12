package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.CageController;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.CageService;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.EmployeeMocks;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CageController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class CageControllerMockMvcTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CageService cageService;
    @MockBean
    EmployeeRepository employeeRepository;
    @MockBean
    Model model;
    CageDto cageDto;
    PartialCageDto partialCageDto;


    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCagesPageMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(cageService.findPaginatedCages(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(cageDtos));

        //THEN
        mockMvc.perform(get("/cages?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/cagePaginated"))
                .andExpect(model().attribute("cagePage", new PageImpl<>(cageDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(cageService, times(1)).findPaginatedCages(PageRequest.of(currentPage - 1, pageSize));
    }


    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCageByIdMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageService.getCageById(1L)).thenReturn(cageDto);

        //THEN
        mockMvc.perform(get("/cages/{cageId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/cageDetails"))
                .andExpect(model().attribute("cage", cageDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(cageService, times(1)).getCageById(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addCageFormMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(employeeRepository.findAllByResponsibilityNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockCaretaker()));

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/cages/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/addCageForm"))
                .andExpect(model().attribute("cage", new PartialCageDto()))
                .andExpect(model().attribute("caretakersAll", Collections.singletonList(EmployeeMocks.mockCaretaker())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveCageMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.addCage(partialCageDto)).thenReturn(cageDto);

        //THEN
        mockMvc.perform(post("/cages").with(csrf()).
                        flashAttr("cage", partialCageDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cages"));
        verify(cageService, times(1)).addCage(partialCageDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCageFormMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.getPartialCageById(1L)).thenReturn(partialCageDto);
        when(employeeRepository.findAllByResponsibilityNotNull()).thenReturn(Collections.singletonList(EmployeeMocks.mockCaretaker()));

        //THEN
        mockMvc.perform(get("/cages/edit/{cageId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/editCageForm"))
                .andExpect(model().attribute("cage", partialCageDto))
                .andExpect(model().attribute("caretakersAll", Collections.singletonList(EmployeeMocks.mockCaretaker())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(cageService, times(1)).getPartialCageById(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCageMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.updateCage(1L, partialCageDto)).thenReturn(cageDto);

        //THEN
        mockMvc.perform(post("/cages/updateCage/{cageId}", 1L).with(csrf()).
                        flashAttr("cage", partialCageDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cages"));
        verify(cageService, times(1)).updateCage(1L, partialCageDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteCageMockMvcTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageService.deleteCage(1L)).thenReturn(true);

        //THEN
        mockMvc.perform(get("/cages/delete/{cageId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cages"));
        verify(cageService, times(1)).deleteCage(1L);
    }

}
