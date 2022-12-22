package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.entity.Diet;
import com.unibuc.main.exception.DietAlreadyExistsException;
import com.unibuc.main.exception.DietNotFoundException;
import com.unibuc.main.mapper.DietMapper;
import com.unibuc.main.repository.DietRepository;
import com.unibuc.main.utils.DietMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DietServiceTest {

    @InjectMocks
    DietService dietService;

    @Mock
    DietRepository dietRepository;

    @Mock
    DietMapper dietMapper;

    Diet diet;

    DietDto dietDto;

    @Test
    public void testGetAllDiets() {
        //GIVEN
        diet = DietMocks.mockDiet();
        dietDto = DietMocks.mockDietDto();

        List<Diet> dietList = new ArrayList<>();
        dietList.add(diet);
        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);

        //WHEN
        when(dietRepository.findAll()).thenReturn(dietList);
        when(dietMapper.mapToDietDto(diet)).thenReturn(dietDto);

        //THEN
        List<DietDto> result = dietService.getAllDiets();
        assertEquals(result, dietDtos);
    }

    @Test
    public void getAllDietsWithEmptyStockTest() {
        //GIVEN
        diet = DietMocks.mockDiet();
        dietDto = DietMocks.mockDietDto();

        List<Diet> dietList = new ArrayList<>();
        dietList.add(diet);
        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);

        //WHEN
        when(dietRepository.findAllDietsWithEmptyStock()).thenReturn(dietList);
        when(dietMapper.mapToDietDto(diet)).thenReturn(dietDto);

        //THEN
        List<DietDto> result = dietService.getAllDietsWithEmptyStock();
        assertEquals(result, dietDtos);
    }

    @Test
    public void testAddNewDiet() {
        //GIVEN
        diet = DietMocks.mockDiet();
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietRepository.findByDietType(TestConstants.DIET_NAME)).thenReturn(Optional.empty());
        when(dietMapper.mapToDiet(dietDto)).thenReturn(diet);
        when(dietMapper.mapToDietDto(diet)).thenReturn(dietDto);
        when(dietRepository.save(diet)).thenReturn(diet);

        //THEN
        DietDto result = dietService.addDiet(dietDto);
        assertEquals(result, dietDto);
        assertThat(result.getDietType()).isNotNull();
        assertThat(result.getQuantityOnStock()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewDietException() {
        //GIVEN
        diet = DietMocks.mockDiet();
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietRepository.findByDietType(TestConstants.DIET_NAME)).thenReturn(Optional.ofNullable(diet));

        //THEN
        DietAlreadyExistsException dietAlreadyExistsException = assertThrows(DietAlreadyExistsException.class, () -> dietService.addDiet(dietDto));
        assertEquals(String.format(ProjectConstants.DIET_EXISTS, TestConstants.DIET_NAME), dietAlreadyExistsException.getMessage());
    }


    @Test
    public void testDeleteDietNotEmpty() {
        //GIVEN
        diet = DietMocks.mockDiet();

        //WHEN
        when(dietRepository.findByDietType(TestConstants.DIET_NAME)).thenReturn(Optional.ofNullable(diet));

        //THEN
        String result = dietService.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME);
        assertEquals(result, ProjectConstants.DIET_NOT_DELETED);
    }

    @Test
    public void testDeleteDietOnlyIfStockEmpty() {
        //GIVEN
        diet = DietMocks.mockDiet();
        diet.setQuantityOnStock(0);

        //WHEN
        when(dietRepository.findByDietType(TestConstants.DIET_NAME)).thenReturn(Optional.ofNullable(diet));

        //THEN
        String result = dietService.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME);
        assertEquals(result, ProjectConstants.DIET_DELETED);
    }



    @Test
    public void testUpdateDiet() {
        //GIVEN
        diet = DietMocks.mockDiet();
        dietDto = DietMocks.mockDietDto();

        Diet updatedDiet = DietMocks.mockDiet();
        dietDto.setQuantityOnStock(5);
        updatedDiet.setQuantityOnStock(5);

        //WHEN
        when(dietRepository.findByDietType(TestConstants.DIET_NAME)).thenReturn(Optional.ofNullable(diet));
        when(dietMapper.mapToDietDto(updatedDiet)).thenReturn(dietDto);
        when(dietRepository.save(updatedDiet)).thenReturn(updatedDiet);

        //THEN
        DietDto result = dietService.updateDietPartial(TestConstants.DIET_NAME, dietDto);
        assertEquals(result, dietDto);
        assertEquals(result.getDietType(), TestConstants.DIET_NAME);
        assertEquals(result.getQuantityOnStock(), 5);
        assertNotNull(result);
    }

    @Test
    public void testUpdateDietException() {
        //GIVEN
        diet = null;
        dietDto = null;

        //WHEN
        when(dietRepository.findByDietType(TestConstants.DIET_NAME)).thenReturn(Optional.empty());

        //THEN
        DietNotFoundException dietNotFoundException = assertThrows(DietNotFoundException.class, () -> dietService.updateDietPartial(TestConstants.DIET_NAME,dietDto));
        assertEquals(String.format(ProjectConstants.DIET_NOT_FOUND, TestConstants.DIET_NAME), dietNotFoundException.getMessage());
    }

}
