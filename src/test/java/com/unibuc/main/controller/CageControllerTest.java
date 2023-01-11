package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.CageService;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.EmployeeMocks;
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
public class CageControllerTest {

    @InjectMocks
    CageController cageController;
    @Mock
    CageService cageService;
    CageDto cageDto;

    @Test
    public void getCagesWithoutACaretakerTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        cageDto.setCaretaker(null);

        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);

        //WHEN
        when(cageService.getCagesWithoutACaretaker()).thenReturn(cageDtos);

        //THEN
        ResponseEntity<List<CageDto>> result = cageController.getCagesWithoutACaretaker();
        assertEquals(result.getBody(), cageDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getCageByIdTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        //WHEN
        when(cageService.getCageById(cageDto.getId())).thenReturn(cageDto);

        //THEN
        ResponseEntity<CageDto> result = cageController.getCageById(cageDto.getId());
        assertEquals(result.getBody(), cageDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewCageTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageService.addCage(cageDto)).thenReturn(cageDto);

        //THEN
        ResponseEntity<CageDto> result = cageController.addNewCage(cageDto);
        assertEquals(result.getBody(), cageDto);
        assertEquals(Objects.requireNonNull(result.getBody()).getId(), cageDto.getId());
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteCageTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when( cageService.deleteCage(cageDto.getId())).thenReturn(true);

        //THEN
        ResponseEntity<String> result = cageController.deleteCage(cageDto.getId());
        assertEquals(result.getBody(), ProjectConstants.OBJ_DELETED);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateCageCaretakerTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        CageDto updatedCage = CageMocks.mockCageDto();
        EmployeeDto caretaker = EmployeeMocks.mockCaretakerDto2();
        updatedCage.setCaretaker(caretaker);

        //WHEN
        when(cageService.updateCageCaretaker(cageDto.getId(), caretaker.getFirstName(), caretaker.getLastName())).thenReturn(updatedCage);

        //THEN
        ResponseEntity<CageDto> result = cageController.updateCageCaretaker(cageDto.getId(), caretaker.getFirstName(), caretaker.getLastName());
        assertEquals(result.getBody(), updatedCage);
        assertEquals(result.getStatusCode().value(), 200);
        assertEquals(Objects.requireNonNull(result.getBody()).getCaretaker(), updatedCage.getCaretaker());
    }

    @Test
    public void updateCagePlacesTest() {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        CageDto updatedCage = CageMocks.mockCageDto();
        updatedCage.setNumberPlaces(5);

        //WHEN
        when(cageService.updatePlacesInCage(cageDto.getId(), 5)).thenReturn(updatedCage);

        //THEN
        ResponseEntity<CageDto> result = cageController.updateCagePlaces(cageDto.getId(), 5);
        assertEquals(result.getBody(), updatedCage);
        assertEquals(result.getStatusCode().value(), 200);
    }
}
