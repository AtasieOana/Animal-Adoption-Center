package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.entity.Cage;
import com.unibuc.main.exception.CageNotFoundException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.mapper.CageMapper;
import com.unibuc.main.repository.CageRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.EmployeeMocks;
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
public class CageServiceTest {

    @InjectMocks
    CageService cageService;

    @Mock
    CageRepository cageRepository;

    @Mock
    CageMapper cageMapper;

    @Mock
    EmployeeRepository employeeRepository;

    Cage cage;

    CageDto cageDto;

    PartialCageDto partialCageDto;

    @Test
    public void testGetCageById() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(cageMapper.mapToCageDto(cage)).thenReturn(cageDto);

        //THEN
        CageDto result = cageService.getCageById(cage.getId());
        assertEquals(result, cageDto);
        assertThat(result).isNotNull();
    }


    @Test
    public void testAddNewCage() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageMapper.mapPartialToCage(partialCageDto)).thenReturn(cage);
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(EmployeeMocks.mockCaretaker()));
        when(cageMapper.mapToCageDto(cage)).thenReturn(cageDto);
        when(cageRepository.save(cage)).thenReturn(cage);

        //THEN
        CageDto result = cageService.addCage(partialCageDto);
        assertEquals(result, cageDto);
        assertThat(result.getNumberPlaces()).isNotNull();
        assertThat(result.getCaretaker()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewCageException() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageMapper.mapPartialToCage(partialCageDto)).thenReturn(cage);
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> cageService.addCage(partialCageDto));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), employeeNotFoundException.getMessage());
    }


    @Test
    public void testDeleteCage() {
        //GIVEN
        cage = CageMocks.mockCage();

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));

        //THEN
        Boolean result = cageService.deleteCage(cage.getId());
        assertEquals(result, true);
    }

    @Test
    public void testDeleteCageException() {
        //GIVEN
        cage = null;

        //WHEN
        when(cageRepository.findById(2L)).thenReturn(Optional.ofNullable(cage));

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> cageService.deleteCage(2L));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, 2L), cageNotFoundException.getMessage());
    }


}
