package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.CaretakerController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.CaretakerService;
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
@WebMvcTest(controllers = CaretakerController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class CaretakerControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CaretakerService caretakerService;
    @MockBean
    Model model;
    EmployeeDto caretakerDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCaretakersPageMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        List<EmployeeDto> caretakerDtos = new ArrayList<>();
        caretakerDtos.add(caretakerDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(caretakerService.findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(caretakerDtos));

        //THEN
        mockMvc.perform(get("/caretakers?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/caretakerPaginated"))
                .andExpect(model().attribute("caretakerPage", new PageImpl<>(caretakerDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(caretakerService, times(1)).findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCaretakerByNameMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(caretakerDto);

        //THEN
        mockMvc.perform(get("/caretakers/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/caretakerDetails"))
                .andExpect(model().attribute("caretaker", caretakerDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(caretakerService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addCaretakerFormMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/caretakers/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/addCaretakerForm"))
                .andExpect(model().attribute("caretaker", new EmployeeDto()))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveCaretakerMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.addNewEmployee(caretakerDto)).thenReturn(caretakerDto);

        //THEN
        mockMvc.perform(post("/caretakers").with(csrf()).
                        flashAttr("caretaker", caretakerDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/caretakers"));
        verify(caretakerService, times(1)).addNewEmployee(caretakerDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCaretakerFormMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(caretakerDto);

        //THEN
        mockMvc.perform(get("/caretakers/edit/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/editCaretakerForm"))
                .andExpect(model().attribute("caretaker", caretakerDto))
                .andExpect(model().attribute("oldFirstName", TestConstants.FIRSTNAME))
                .andExpect(model().attribute("oldLastName", TestConstants.LASTNAME))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(caretakerService, times(1)).getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCaretakerMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto)).thenReturn(caretakerDto);

        //THEN
        mockMvc.perform(post("/caretakers/updateCaretaker/{oldFirstName}/{oldLastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME).with(csrf()).
                        flashAttr("caretaker", caretakerDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/caretakers"));
        verify(caretakerService, times(1)).updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteEmployeeMockMvcTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        mockMvc.perform(get("/caretakers/delete/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/caretakers"));
        verify(caretakerService, times(1)).deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
    }
}
