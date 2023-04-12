package com.unibuc.main.controller.IT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.utils.VaccineMocks;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@ActiveProfiles("h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class VaccineControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private VaccineRepository vaccineRepository;

    @Test
    @Order(1)
    public void addVaccinesTest() {
        Vaccine vaccine = VaccineMocks.mockVaccine();
        vaccineRepository.save(vaccine);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVaccinesPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/vaccines?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/vaccinePaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getVaccineByNameTest() throws Exception {
        mockMvc.perform(get("/vaccines/{vaccineId}", TestConstants.VACCINE_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/vaccineDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addVaccineFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/vaccines/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/addVaccineForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveVaccineTest() throws Exception {
        mockMvc.perform(post("/vaccines").with(csrf()).
                        flashAttr("vaccine", VaccineMocks.mockVaccineDto2()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vaccines"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVaccineFormTest() throws Exception {
        mockMvc.perform(get("/vaccines/edit/{vaccineId}", TestConstants.VACCINE_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/vaccineTemplates/editVaccineForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editVaccineTest() throws Exception {
        PartialVaccineDto editVaccineDto = VaccineMocks.mockPartialVaccineDto();
        mockMvc.perform(post("/vaccines/updateVaccine/{vaccineId}", TestConstants.VACCINE_NAME).with(csrf()).
                        flashAttr("vaccine", editVaccineDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vaccines"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteVaccineTest() throws Exception {
        //THEN
        mockMvc.perform(get("/vaccines/deleteExpiredVaccines"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vaccines"));
    }
}
