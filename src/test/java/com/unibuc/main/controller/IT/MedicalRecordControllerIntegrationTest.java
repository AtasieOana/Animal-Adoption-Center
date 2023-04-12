package com.unibuc.main.controller.IT;

import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.repository.*;
import com.unibuc.main.utils.*;
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
public class MedicalRecordControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private DietRepository dietRepository;

    @Test
    @Order(1)
    public void addMedicalRecordTest() {
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        cageRepository.save(CageMocks.mockCage());
        dietRepository.save(DietMocks.mockDiet());
        animalRepository.save(AnimalMocks.mockAnimal());
        employeeRepository.save(EmployeeMocks.mockVet());

        MedicalRecord medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordRepository.save(medicalRecord);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getMedicalRecordsPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/medicalRecords?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/medicalRecordPaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getMedicalRecordByTypeTest() throws Exception {
        mockMvc.perform(get("/medicalRecords/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/medicalRecordDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addMedicalRecordFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/medicalRecords/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/addMedicalRecordForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveMedicalRecordTest() throws Exception {
        mockMvc.perform(post("/medicalRecords").with(csrf()).
                        flashAttr("medicalRecord", MedicalRecordMocks.mockAddMedicalRecordDto()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicalRecords"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editMedicalRecordFormTest() throws Exception {
        mockMvc.perform(get("/medicalRecords/edit/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/medicalRecordTemplates/editMedicalRecordForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editMedicalRecordTest() throws Exception {
        mockMvc.perform(post("/medicalRecords/updateMedicalRecord/{medicalRecordId}", 1L).with(csrf()).
                        flashAttr("medicalRecord", MedicalRecordMocks.mockEditMedicalRecordDto()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicalRecords"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteMedicalRecordTest() throws Exception {
        //THEN
        mockMvc.perform(get("/medicalRecords/delete/{medicalRecordId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicalRecords"));
    }
}
