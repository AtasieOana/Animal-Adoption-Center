package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.DietController;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.DietMocks;
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
@WebMvcTest(controllers = DietController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class DietControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    DietService dietService;
    @MockBean
    Model model;
    DietDto dietDto;

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getDietsPageMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(dietService.findPaginatedDiets(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(dietDtos));

        //THEN
        mockMvc.perform(get("/diets?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/dietPaginated"))
                .andExpect(model().attribute("dietPage", new PageImpl<>(dietDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(dietService, times(1)).findPaginatedDiets(PageRequest.of(currentPage - 1, pageSize));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getDietByTypeMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.getDietByType(TestConstants.DIET_NAME)).thenReturn(dietDto);

        //THEN
        mockMvc.perform(get("/diets/{dietType}", TestConstants.DIET_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/dietDetails"))
                .andExpect(model().attribute("diet", dietDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(dietService, times(1)).getDietByType(TestConstants.DIET_NAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addDietFormMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/diets/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/addDietForm"))
                .andExpect(model().attribute("diet", new DietDto()))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveDietMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.addDiet(dietDto)).thenReturn(dietDto);

        //THEN
        mockMvc.perform(post("/diets").with(csrf()).
                    flashAttr("diet", dietDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/diets"));
        verify(dietService, times(1)).addDiet(dietDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editDietFormMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.getDietByType(TestConstants.DIET_NAME)).thenReturn(dietDto);

        //THEN
        mockMvc.perform(get("/diets/edit/{oldDietType}", TestConstants.DIET_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name("/dietTemplates/editDietForm"))
                .andExpect(model().attribute("diet", dietDto))
                .andExpect(model().attribute("oldDietType", TestConstants.DIET_NAME))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(dietService, times(1)).getDietByType(TestConstants.DIET_NAME);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editDietMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.updateDietPartial(TestConstants.DIET_NAME, dietDto)).thenReturn(dietDto);

        //THEN
        mockMvc.perform(post("/diets/updateDiet/{oldDietType}", TestConstants.DIET_NAME).with(csrf()).
                        flashAttr("diet", dietDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/diets"));
        verify(dietService, times(1)).updateDietPartial(TestConstants.DIET_NAME, dietDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteDietIfStockEmptyMockMvcTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME)).thenReturn(ProjectConstants.DIET_NOT_DELETED);

        //THEN
        mockMvc.perform(get("/diets/deleteIfStockEmpty/{dietType}", TestConstants.DIET_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("result", ProjectConstants.DIET_NOT_DELETED))
                .andExpect(view().name("redirect:/diets"));
        verify(dietService, times(1)).deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME);
    }
}
