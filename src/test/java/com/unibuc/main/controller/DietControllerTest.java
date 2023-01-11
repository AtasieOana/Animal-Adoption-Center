package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.DietMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DietControllerTest {

    @InjectMocks
    DietController dietController;
    @Mock
    DietService dietService;
    DietDto dietDto;

    @Test
    public void getAllDietsTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);

        //WHEN
        when(dietService.getAllDiets()).thenReturn(dietDtos);

        //THEN
        ResponseEntity<List<DietDto>> result = dietController.getAllDiets();
        assertEquals(result.getBody(), dietDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getAllDietsWithEmptyStockTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);

        //WHEN
        when(dietService.getAllDietsWithEmptyStock()).thenReturn(dietDtos);

        //THEN
        ResponseEntity<List<DietDto>> result = dietController.getAllDietsWithEmptyStock();
        assertEquals(result.getBody(), dietDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewDietTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        //WHEN
        when(dietService.addDiet(dietDto)).thenReturn(dietDto);

        //THEN
        ResponseEntity<DietDto> result = dietController.addNewDiet(dietDto);
        assertEquals(result.getBody(), dietDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteExpiredDietsTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        dietDto.setQuantityOnStock(0);

        //WHEN
        when( dietService.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME)).thenReturn(ProjectConstants.DIET_DELETED);

        //THEN
        ResponseEntity<String> result = dietController.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME);
        assertEquals(result.getBody(), ProjectConstants.DIET_DELETED);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateDietTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        DietDto updatedDiet = DietMocks.mockDietDto();
        updatedDiet.setQuantityOnStock(10);

        //WHEN
        when(dietService.updateDietPartial(TestConstants.DIET_NAME, dietDto)).thenReturn(updatedDiet);

        //THEN
        ResponseEntity<DietDto> result = dietController.updateDiet(TestConstants.DIET_NAME, dietDto);
        assertEquals(result.getBody(), updatedDiet);
        assertEquals(Objects.requireNonNull(result.getBody()).getQuantityOnStock(), 10);
        assertEquals(result.getStatusCode().value(), 200);
    }

}
