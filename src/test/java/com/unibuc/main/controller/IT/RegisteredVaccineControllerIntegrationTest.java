package com.unibuc.main.controller.IT;

import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.entity.RegisteredVaccine;
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
public class RegisteredVaccineControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private RegisteredVaccineRepository registeredVaccineRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Test
    @Order(1)
    public void addRegisteredVaccineTest() {
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        employeeRepository.save(EmployeeMocks.mockVet());
        cageRepository.save(CageMocks.mockCage());
        dietRepository.save(DietMocks.mockDiet());
        animalRepository.save(AnimalMocks.mockAnimal());
        animalRepository.save(AnimalMocks.mockAnimal2());
        vaccineRepository.save(VaccineMocks.mockVaccine());
        medicalRecordRepository.save(MedicalRecordMocks.mockMedicalRecord());
        medicalRecordRepository.save(MedicalRecordMocks.mockMedicalRecord2());

        RegisteredVaccine registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineRepository.save(registeredVaccine);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getRegisteredVaccinesPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/registeredVaccines?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/registeredVaccinePaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getRegisteredVaccineByTypeTest() throws Exception {
        mockMvc.perform(get("/registeredVaccines/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/registeredVaccineDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addRegisteredVaccineFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/registeredVaccines/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/addRegisteredVaccineForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveRegisteredVaccineTest() throws Exception {
        PartialRegisteredVaccineDto addRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto2();
        mockMvc.perform(post("/registeredVaccines").with(csrf()).
                        flashAttr("registeredVaccine", addRegisteredVaccineDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registeredVaccines"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editRegisteredVaccineFormTest() throws Exception {
        mockMvc.perform(get("/registeredVaccines/edit/{medicalRecordId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/registeredVaccineTemplates/editRegisteredVaccineForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editRegisteredVaccineTest() throws Exception {
        mockMvc.perform(post("/registeredVaccines/updateRegisteredVaccine/{medicalRecordId}", 1L).with(csrf()).
                        flashAttr("registeredVaccine", RegisteredVaccineMocks.mockPartialRegisteredVaccineDto()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registeredVaccines"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteRegisteredVaccineTest() throws Exception {
        //THEN
        mockMvc.perform(get("/registeredVaccines/delete/{medicalRecordId}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registeredVaccines"));
    }
}
