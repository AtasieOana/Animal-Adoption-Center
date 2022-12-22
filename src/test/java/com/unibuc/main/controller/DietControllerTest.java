package com.unibuc.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.DietMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = DietController.class)
public class DietControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    DietService dietService;
    DietDto dietDto;

    @Test
    public void getAllDietsTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);

        //WHEN
        when(dietService.getAllDiets()).thenReturn(dietDtos);

        //THEN
        mockMvc.perform(get("/diets/getAllDiets"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(dietDtos)));
    }

    @Test
    public void getAllDietsWithEmptyStockTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);

        //WHEN
        when(dietService.getAllDietsWithEmptyStock()).thenReturn(dietDtos);

        //THEN
        mockMvc.perform(get("/diets/getAllDietsWithEmptyStock"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(dietDtos)));
    }

    @Test
    public void addNewDietTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        //WHEN
        when(dietService.addDiet(dietDto)).thenReturn(dietDto);

        //THEN
        mockMvc.perform(post("/diets")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dietDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dietType").value(dietDto.getDietType()))
                .andExpect(jsonPath("$.quantityOnStock").value(6));
    }

    @Test
    public void deleteExpiredDietsTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        dietDto.setQuantityOnStock(0);

        //WHEN
        when( dietService.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME)).thenReturn(ProjectConstants.DIET_DELETED);

        //THEN
        mockMvc.perform(delete("/diets/deleteIfStockEmpty/" + TestConstants.DIET_NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.DIET_DELETED));
    }

    @Test
    public void updateDietTest() throws Exception {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        DietDto updatedDiet = dietDto;
        updatedDiet.setQuantityOnStock(10);

        //WHEN
        when(dietService.updateDietPartial(TestConstants.DIET_NAME, dietDto)).thenReturn(updatedDiet);

        //THEN
        mockMvc.perform(put("/diets/" + TestConstants.DIET_NAME)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dietDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedDiet)))
                .andExpect(jsonPath("$.dietType").value(dietDto.getDietType()))
                .andExpect(jsonPath("$.quantityOnStock").value(10));
    }

}
