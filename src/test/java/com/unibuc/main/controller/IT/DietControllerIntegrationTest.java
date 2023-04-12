package com.unibuc.main.controller.IT;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.entity.Diet;
import com.unibuc.main.repository.DietRepository;
import com.unibuc.main.utils.DietMocks;
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
public class DietControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private DietRepository dietRepository;

    @Test
    @Order(1)
    public void addDietTest() {
        Diet diet = DietMocks.mockDiet();
        dietRepository.save(diet);
    }


    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getDietsPageTest() throws Exception {
        int currentPage = 1;
        int pageSize = 5;

        mockMvc.perform(get("/diets?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/dietPaginated"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getDietByTypeTest() throws Exception {
        mockMvc.perform(get("/diets/{dietType}", TestConstants.DIET_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/dietDetails"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addDietFormTest() throws Exception {
        mockMvc.perform(request(HttpMethod.POST, "/diets/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/addDietForm"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveDietTest() throws Exception {
        DietDto addDietDto = DietMocks.mockDietDto();
        addDietDto.setDietType("Fruit");
        mockMvc.perform(post("/diets").with(csrf()).
                        flashAttr("diet", addDietDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/diets"));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveDietExceptionTest() throws Exception {
        DietDto addDietDto = DietMocks.mockDietDto();
        mockMvc.perform(post("/diets").with(csrf()).
                        flashAttr("diet", addDietDto))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/addDietForm"));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editDietFormTest() throws Exception {
        mockMvc.perform(get("/diets/edit/{oldDietType}", TestConstants.DIET_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/editDietForm"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editDietTest() throws Exception {
        DietDto editDietDto = DietMocks.mockDietDto();
        editDietDto.setQuantityOnStock(10);
        mockMvc.perform(post("/diets/updateDiet/{oldDietType}", TestConstants.DIET_NAME).with(csrf()).
                        flashAttr("diet", editDietDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/diets"));
    }

    @Test
    @Order(9)
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteDietIfStockEmptyTest() throws Exception {
        //THEN
        mockMvc.perform(get("/diets/deleteIfStockEmpty/{dietType}", TestConstants.DIET_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/diets"));
    }
}
