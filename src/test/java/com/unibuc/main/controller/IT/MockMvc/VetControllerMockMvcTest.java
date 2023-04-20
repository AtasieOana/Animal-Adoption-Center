package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.VetController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.VetService;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = VetController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class VetControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    VetService vetService;
    @MockBean
    Model model;
    EmployeeDto vetDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVetsPageMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();
        List<EmployeeDto> vetDtos = new ArrayList<>();
        vetDtos.add(vetDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(vetService.findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize), pageSize, currentPage-1)).thenReturn(new PageImpl<>(vetDtos));

        //THEN
        mockMvc.perform(get("/vets?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/vetPaginated"))
                .andExpect(model().attribute("vetPage", new PageImpl<>(vetDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(vetService, times(1)).findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize), pageSize, currentPage-1);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVetByNameMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(vetDto);

        //THEN
        mockMvc.perform(get("/vets/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/vetDetails"))
                .andExpect(model().attribute("vet", vetDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(vetService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addVetFormMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/vets/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/addVetForm"))
                .andExpect(model().attribute("vet", new EmployeeDto()))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveVetMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.addNewEmployee(vetDto)).thenReturn(vetDto);

        //THEN
        mockMvc.perform(post("/vets").with(csrf()).
                        flashAttr("vet", vetDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vets"));
        verify(vetService, times(1)).addNewEmployee(vetDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVetFormMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(vetDto);

        //THEN
        mockMvc.perform(get("/vets/edit/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/editVetForm"))
                .andExpect(model().attribute("vet", vetDto))
                .andExpect(model().attribute("oldFirstName", TestConstants.FIRSTNAME))
                .andExpect(model().attribute("oldLastName", TestConstants.LASTNAME_VET))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(vetService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVetMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto)).thenReturn(vetDto);

        //THEN
        mockMvc.perform(post("/vets/updateVet/{oldFirstName}/{oldLastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET).with(csrf()).
                        flashAttr("vet", vetDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vets"));
        verify(vetService, times(1)).updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteEmployeeMockMvcTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(true);

        //THEN
        mockMvc.perform(get("/vets/delete/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vets"));
        verify(vetService, times(1)).deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
    }
}
