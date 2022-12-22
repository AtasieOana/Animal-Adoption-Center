package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.CageDto;
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
    public void testGetCagesWithoutACaretaker() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();

        List<Cage> cageList = new ArrayList<>();
        cageList.add(cage);
        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);

        //WHEN
        when(cageRepository.findAllByCaretakerNull()).thenReturn(cageList);
        when(cageMapper.mapToCageDto(cage)).thenReturn(cageDto);

        //THEN
        List<CageDto> result = cageService.getCagesWithoutACaretaker();
        assertEquals(result, cageDtos);
    }

    @Test
    public void testAddNewCage() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageMapper.mapToCage(cageDto)).thenReturn(cage);
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(EmployeeMocks.mockCaretaker()));
        when(cageMapper.mapToCageDto(cage)).thenReturn(cageDto);
        when(cageRepository.save(cage)).thenReturn(cage);

        //THEN
        CageDto result = cageService.addCage(cageDto);
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

        //WHEN
        when(cageMapper.mapToCage(cageDto)).thenReturn(cage);
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> cageService.addCage(cageDto));
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

    @Test
    public void testUpdatePlacesInCage() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        cageDto.setNumberPlaces(5);
        Cage updatedCage = CageMocks.mockCage();
        updatedCage.setNumberPlaces(5);

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(cageMapper.mapToCageDto(updatedCage)).thenReturn(cageDto);
        when(cageRepository.save(updatedCage)).thenReturn(updatedCage);

        //THEN
        CageDto result = cageService.updatePlacesInCage(cage.getId(), 5);
        assertEquals(result, cageDto);
        assertEquals(result.getCaretaker(), EmployeeMocks.mockCaretakerDto());
        assertEquals(result.getNumberPlaces(), 5);
        assertNotNull(result);
    }

    @Test
    public void testUpdatePlacesInCageException() {
        //GIVEN
        cage = null;
        cageDto = null;

        //WHEN
        when(cageRepository.findById(2L)).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> cageService.updatePlacesInCage(2L, 10));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, 2), cageNotFoundException.getMessage());
    }

    @Test
    public void testUpdateCageCaretaker() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        Cage updatedCage = CageMocks.mockCage();
        updatedCage.setCaretaker(EmployeeMocks.mockCaretaker2());
        cageDto.setCaretaker(EmployeeMocks.mockCaretakerDto2());

        // WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME2, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(EmployeeMocks.mockCaretaker2()));
        when(cageMapper.mapToCageDto(updatedCage)).thenReturn(cageDto);
        when(cageRepository.save(updatedCage)).thenReturn(updatedCage);

        // THEN
        CageDto result = cageService.updateCageCaretaker(cage.getId(), TestConstants.FIRSTNAME2, TestConstants.LASTNAME);
        assertEquals(result, cageDto);
        assertNotEquals(result.getCaretaker(), EmployeeMocks.mockCaretakerDto());
        assertEquals(result.getCaretaker(), EmployeeMocks.mockCaretakerDto2());
        assertNotNull(result);
    }

    @Test
    public void testUpdateCageCaretakerExceptionNotCage() {
        //GIVEN
        cage = null;
        cageDto = null;

        //WHEN
        when(cageRepository.findById(2L)).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> cageService.updateCageCaretaker(2L, TestConstants.FIRSTNAME2, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, 2), cageNotFoundException.getMessage());
    }

    @Test
    public void testUpdateCageCaretakerExceptionNotCaretaker() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME2, TestConstants.LASTNAME)).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> cageService.updateCageCaretaker(cage.getId(), TestConstants.FIRSTNAME2, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME2 + " " + TestConstants.LASTNAME), employeeNotFoundException.getMessage());
    }

}
