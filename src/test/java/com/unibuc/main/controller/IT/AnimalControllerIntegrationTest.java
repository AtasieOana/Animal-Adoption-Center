package com.unibuc.main.controller.IT;

import com.unibuc.main.dto.AddAnimalDto;
import com.unibuc.main.dto.AdoptAnimalDto;
import com.unibuc.main.entity.Animal;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@ActiveProfiles("h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AnimalControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private CageRepository cageRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Order(1)
    public void addAnimalsTest() {
        employeeRepository.deleteAll();
        employeeRepository.save(EmployeeMocks.mockCaretaker());
        log.info("Employees saved: " + employeeRepository.findAll());

        cageRepository.save(CageMocks.mockCage());
        dietRepository.save(DietMocks.mockDiet());
        clientRepository.save(ClientMocks.mockClient());

        Animal animal = AnimalMocks.mockAnimal();
        animalRepository.save(animal);
        log.info("Animals saved: " + animalRepository.findAll());
    }


    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getAnimalsPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/animals?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/animalPaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getAnimalByIdTest() throws Exception {
        mockMvc.perform(get("/animals/{animalId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/animalDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addAnimalFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/animals/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/addAnimalForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveAnimalTest() throws Exception {
        AddAnimalDto addAnimalDto = AnimalMocks.mockAddAnimalDto();
        mockMvc.perform(post("/animals").with(csrf()).
                        flashAttr("animal", addAnimalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animals"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editAnimalFormTest() throws Exception {
        mockMvc.perform(get("/animals/edit/{animalId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/editAnimalForm"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editAnimalTest() throws Exception {
        AdoptAnimalDto adoptAnimalDto = AnimalMocks.mockAdoptAnimalDto();
        mockMvc.perform(post("/animals/adoptAnimal/{animalId}", 1L).with(csrf()).
                        flashAttr("animal", adoptAnimalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animals"));
    }

    @Test
    @Order(9)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteAnimalTest() throws Exception {
        //THEN
        mockMvc.perform(get("/animals/deleteAdoptedAnimals"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animals"));
    }

    @Test
    @Order(10)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getOldestAnimalInCenterTest() throws Exception {
        mockMvc.perform(get("/animals/getOldestAnimalInCenter"))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/animalDetails"));
    }
}
