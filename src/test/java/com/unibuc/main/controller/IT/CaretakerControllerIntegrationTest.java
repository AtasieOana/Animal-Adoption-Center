package com.unibuc.main.controller.IT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.utils.EmployeeMocks;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@ActiveProfiles("h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CaretakerControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addEmployeeTest() {
        Employee caretaker = EmployeeMocks.mockCaretaker();
        employeeRepository.save(caretaker);
        Employee vet = EmployeeMocks.mockVet();
        employeeRepository.save(vet);
    }


    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCaretakersPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/caretakers?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/caretakerPaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCaretakerByNameTest() throws Exception {
        mockMvc.perform(get("/caretakers/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/caretakerDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addCaretakerFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/caretakers/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/addCaretakerForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveCaretakerTest() throws Exception {
        mockMvc.perform(post("/caretakers").with(csrf()).
                        flashAttr("caretaker", EmployeeMocks.mockCaretakerDto2()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/caretakers"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCaretakerFormTest() throws Exception {
        mockMvc.perform(get("/caretakers/edit/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/editCaretakerForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCaretakerTest() throws Exception {
        EmployeeDto employeeDto = EmployeeMocks.mockCaretakerDto();
        employeeDto.setPhoneNumber("0765881925");
        mockMvc.perform(post("/caretakers/updateCaretaker/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME).with(csrf()).
                        flashAttr("caretaker", employeeDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/caretakers"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteCaretakerTest() throws Exception {
        //THEN
        mockMvc.perform(get("/caretakers/delete/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/caretakers"));
    }
}
