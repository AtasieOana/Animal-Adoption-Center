package com.unibuc.main.controller.IT;

import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.entity.Cage;
import com.unibuc.main.repository.CageRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.EmployeeMocks;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CageControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addCageTest() {
        Cage cage = CageMocks.mockCage();
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        cageRepository.save(cage);
    }


    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCagesPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/cages?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/cagePaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getCageByTypeTest() throws Exception {
        mockMvc.perform(get("/cages/{cageId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/cageDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addCageFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/cages/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/addCageForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveCageTest() throws Exception {
        PartialCageDto addCageDto = CageMocks.mockPartialCageDto();
        mockMvc.perform(post("/cages").with(csrf()).
                        flashAttr("cage", addCageDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cages"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCageFormTest() throws Exception {
        mockMvc.perform(get("/cages/edit/{cageId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/cageTemplates/editCageForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editCageTest() throws Exception {
        PartialCageDto editCageDto = CageMocks.mockPartialCageDto();
        mockMvc.perform(post("/cages/updateCage/{cageId}", 1L).with(csrf()).
                        flashAttr("cage", editCageDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cages"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteCageTest() throws Exception {
        //THEN
        mockMvc.perform(get("/cages/delete/{cageId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cages"));
    }
}
