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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@ActiveProfiles("h2")
@Slf4j
public class VetControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addEmployeeTest() {
        employeeRepository.deleteAll();
        Employee caretaker = EmployeeMocks.mockCaretaker();
        employeeRepository.save(caretaker);
        Employee vet = EmployeeMocks.mockVet();
        employeeRepository.save(vet);
        log.info(employeeRepository.findAll().toString());
    }


    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVetsPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/vets?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/vetPaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVetByNameTest() throws Exception {
        mockMvc.perform(get("/vets/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/vetDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addVetFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/vets/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/addVetForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveVetTest() throws Exception {
        mockMvc.perform(post("/vets").with(csrf()).
                        flashAttr("vet", EmployeeMocks.mockVetDto2()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vets"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVetFormTest() throws Exception {
        mockMvc.perform(get("/vets/edit/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET))
                .andExpect(status().isOk())
                .andExpect(view().name("/employeeTemplates/editVetForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVetTest() throws Exception {
        EmployeeDto employeeDto = EmployeeMocks.mockVetDto();
        employeeDto.setPhoneNumber("0765881925");
        mockMvc.perform(post("/vets/updateVet/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET).with(csrf()).
                        flashAttr("vet", employeeDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vets"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteVetTest() throws Exception {
        //THEN
        mockMvc.perform(get("/vets/delete/{firstName}/{lastName}", TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vets"));
    }
}
