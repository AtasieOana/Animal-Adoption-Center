package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.exception.VaccineAlreadyExistsException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.VaccineMapper;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.utils.VaccineMocks;
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
public class VaccineServiceTest {

    @InjectMocks
    VaccineService vaccineService;

    @Mock
    VaccineRepository vaccineRepository;

    @Mock
    VaccineMapper vaccineMapper;

    Vaccine vaccine;

    VaccineDto vaccineDto;
    PartialVaccineDto partialVaccineDto;

    @Test
    public void testGetAllVaccinesOrderByExpiredDate() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();

        List<Vaccine> vaccineList = new ArrayList<>();
        vaccineList.add(vaccine);
        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);

        //WHEN
        when(vaccineRepository.findAllOrderByExpirationDate()).thenReturn(vaccineList);
        when(vaccineMapper.mapToVaccineDto(vaccine)).thenReturn(vaccineDto);

        //THEN
        List<VaccineDto> result = vaccineService.getAllVaccinesOrderByExpiredDate();
        assertEquals(result, vaccineDtos);
    }


    @Test
    public void testAddNewVaccine() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineRepository.findByVaccineName(TestConstants.VACCINE_NAME)).thenReturn(Optional.empty());
        when(vaccineMapper.mapToVaccine(vaccineDto)).thenReturn(vaccine);
        when(vaccineMapper.mapToVaccineDto(vaccine)).thenReturn(vaccineDto);
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);

        //THEN
        VaccineDto result = vaccineService.addVaccine(vaccineDto);
        assertEquals(result, vaccineDto);
        assertThat(result.getVaccineName()).isNotNull();
        assertThat(result.getExpirationDate()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewVaccineException() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineRepository.findByVaccineName(TestConstants.VACCINE_NAME)).thenReturn(Optional.ofNullable(vaccine));

        //THEN
        VaccineAlreadyExistsException vaccineAlreadyExistsException = assertThrows(VaccineAlreadyExistsException.class, () -> vaccineService.addVaccine(vaccineDto));
        assertEquals(String.format(ProjectConstants.VACCINE_EXISTS, TestConstants.VACCINE_NAME), vaccineAlreadyExistsException.getMessage());
    }


    @Test
    public void testDeleteExpiredVaccines() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();
        List<Vaccine> vaccineList = new ArrayList<>();
        vaccineList.add(vaccine);

        //WHEN
        when(vaccineRepository.findAllByExpirationDateBefore()).thenReturn(vaccineList);

        //THEN
        String result = vaccineService.deleteExpiredVaccines();
        assertEquals(result, ProjectConstants.DELETED_EXP_VACCINES);
    }

    @Test
    public void testDeleteExpiredVaccinesEmpty() {
        //GIVEN
        vaccine = null;
        vaccineDto = null;
        List<Vaccine> vaccineList = new ArrayList<>();

        //WHEN
        when(vaccineRepository.findAllByExpirationDateBefore()).thenReturn(vaccineList);

        //THEN
        String result = vaccineService.deleteExpiredVaccines();
        assertEquals(result, ProjectConstants.NO_EXP_VACCINES);
    }


    @Test
    public void testUpdateVaccine() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();
        partialVaccineDto = VaccineMocks.mockPartialVaccineDto();
        partialVaccineDto.setQuantityOnStock(5);

        Vaccine updatedVaccine = VaccineMocks.mockVaccine();
        updatedVaccine.setQuantityOnStock(5);
        vaccineDto.setQuantityOnStock(5);

        //WHEN
        when(vaccineRepository.findByVaccineName(TestConstants.VACCINE_NAME)).thenReturn(Optional.ofNullable(vaccine));
        when(vaccineMapper.mapToVaccineDto(updatedVaccine)).thenReturn(vaccineDto);
        when(vaccineRepository.save(updatedVaccine)).thenReturn(updatedVaccine);

        //THEN
        VaccineDto result = vaccineService.updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto);
        assertEquals(result, vaccineDto);
        assertEquals(result.getVaccineName(), TestConstants.VACCINE_NAME);
        assertEquals(result.getQuantityOnStock(), 5);
        assertNotNull(result);
    }

    @Test
    public void testUpdateVaccineException() {
        //GIVEN
        vaccine = null;
        vaccineDto = null;

        //WHEN
        when(vaccineRepository.findByVaccineName(TestConstants.VACCINE_NAME)).thenReturn(Optional.empty());

        //THEN
        VaccineNotFoundException vaccineNotFoundException = assertThrows(VaccineNotFoundException.class, () -> vaccineService.updateVaccine(TestConstants.VACCINE_NAME,partialVaccineDto));
        assertEquals(String.format(ProjectConstants.VACCINE_NOT_FOUND, TestConstants.VACCINE_NAME), vaccineNotFoundException.getMessage());
    }

    @Test
    public void testGetVaccineByName() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineRepository.findByVaccineName(vaccine.getVaccineName())).thenReturn(Optional.ofNullable(vaccine));
        when(vaccineMapper.mapToVaccineDto(vaccine)).thenReturn(vaccineDto);

        //THEN
        VaccineDto result = vaccineService.getVaccineByName(vaccine.getVaccineName());
        assertEquals(result, vaccineDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void testGetVaccineByNameException() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineRepository.findByVaccineName(vaccine.getVaccineName())).thenReturn(Optional.empty());

        //THEN
        VaccineNotFoundException vaccineNotFoundException = assertThrows(VaccineNotFoundException.class, () -> vaccineService.getVaccineByName(vaccine.getVaccineName()));
        assertEquals(String.format(ProjectConstants.VACCINE_NOT_FOUND, vaccine.getVaccineName()), vaccineNotFoundException.getMessage());
    }

    @Test
    public void testFindPaginatedVaccines() {
        //GIVEN
        vaccine = VaccineMocks.mockVaccine();
        vaccineDto = VaccineMocks.mockVaccineDto();
        Pageable pageable = PageRequest.of(0,20);

        List<Vaccine> vaccineList = new ArrayList<>();
        vaccineList.add(vaccine);
        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);

        //WHEN
        when(vaccineRepository.findAll(pageable)).thenReturn(new PageImpl<>(vaccineList));
        when(vaccineMapper.mapToVaccineDto(vaccine)).thenReturn(vaccineDto);

        //THEN
        Page<VaccineDto> result = vaccineService.findPaginatedVaccines(pageable);
        assertEquals(result, new PageImpl<>(vaccineDtos));
    }
}
