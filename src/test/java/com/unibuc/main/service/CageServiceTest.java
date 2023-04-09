package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public void testGetAllCages() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();

        List<Cage> cages = new ArrayList<>();
        cages.add(cage);
        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);

        //WHEN
        when(cageRepository.findAll()).thenReturn(cages);
        when(cageMapper.mapToCageDto(cage)).thenReturn(cageDto);

        //THEN
        List<CageDto> result = cageService.getAllCages();
        assertEquals(result, cageDtos);
    }

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
    public void testGetCageByIdException() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> cageService.getCageById(cage.getId()));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, cage.getId()), cageNotFoundException.getMessage());
    }

    @Test
    public void testAddNewCage() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageMapper.mapPartialToCage(partialCageDto)).thenReturn(cage);
        when(employeeRepository.findById(partialCageDto.getCaretakerId())).thenReturn(Optional.ofNullable(EmployeeMocks.mockCaretaker()));
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
        when(employeeRepository.findById(partialCageDto.getCaretakerId())).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> cageService.addCage(partialCageDto));
        assertEquals(String.format(ProjectConstants.EMP_ID_NOT_FOUND, partialCageDto.getCaretakerId()), employeeNotFoundException.getMessage());
    }

    @Test
    public void testUpdateCage() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        cageDto.setNumberPlaces(5);
        partialCageDto = CageMocks.mockPartialCageDto();
        partialCageDto.setNumberPlaces(5);

        Cage updatedCage = CageMocks.mockCage();
        updatedCage.setNumberPlaces(5);

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(employeeRepository.findById(partialCageDto.getCaretakerId())).thenReturn(Optional.ofNullable(EmployeeMocks.mockCaretaker()));
        when(cageRepository.save(updatedCage)).thenReturn(updatedCage);
        when(cageMapper.mapToCageDto(updatedCage)).thenReturn(cageDto);

        //THEN
        CageDto result = cageService.updateCage(cage.getId(), partialCageDto);
        assertEquals(result, cageDto);
        assertEquals(result.getNumberPlaces(), 5);
        assertThat(result.getNumberPlaces()).isNotNull();
        assertThat(result.getCaretaker()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testUpdateCageException() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        cageDto.setNumberPlaces(5);
        partialCageDto = CageMocks.mockPartialCageDto();
        partialCageDto.setNumberPlaces(5);

        //WHEN
        when(cageRepository.findById(2L)).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> cageService.updateCage(2L, partialCageDto));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, 2L), cageNotFoundException.getMessage());
    }

    @Test
    public void testUpdateCageEmployeeException() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        cageDto.setNumberPlaces(5);
        partialCageDto = CageMocks.mockPartialCageDto();
        partialCageDto.setNumberPlaces(5);

        Cage updatedCage = CageMocks.mockCage();
        updatedCage.setNumberPlaces(5);

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(employeeRepository.findById(partialCageDto.getCaretakerId())).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> cageService.updateCage(cage.getId(), partialCageDto));
        assertEquals(String.format(ProjectConstants.EMP_ID_NOT_FOUND, partialCageDto.getCaretakerId()), employeeNotFoundException.getMessage());
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
    public void testFindPaginatedCages() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();
        Pageable pageable = PageRequest.of(0,20);

        List<Cage> cages = new ArrayList<>();
        cages.add(cage);
        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);

        //WHEN
        when(cageRepository.findAll(pageable)).thenReturn(new PageImpl<>(cages));
        when(cageMapper.mapToCageDto(cage)).thenReturn(cageDto);

        //THEN
        Page<CageDto> result = cageService.findPaginatedCages(pageable);
        assertEquals(result, new PageImpl<>(cageDtos));
    }

    @Test
    public void testGetPartialCageById() {
        //GIVEN
        cage = CageMocks.mockCage();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(cage));
        when(cageMapper.mapCageToPartial(cage)).thenReturn(partialCageDto);

        //THEN
        PartialCageDto result = cageService.getPartialCageById(cage.getId());
        assertEquals(result, partialCageDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void testGetPartialCageByIdException() {
        //GIVEN
        cage = CageMocks.mockCage();
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> cageService.getPartialCageById(cage.getId()));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, cage.getId()), cageNotFoundException.getMessage());
    }
}
