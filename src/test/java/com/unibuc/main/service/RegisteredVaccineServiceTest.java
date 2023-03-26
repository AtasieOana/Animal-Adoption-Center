package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.exception.RegisteredVaccineNotFoundException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.RegisteredVaccineRepository;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.utils.MedicalRecordMocks;
import com.unibuc.main.utils.RegisteredVaccineMocks;
import com.unibuc.main.utils.VaccineMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisteredVaccineServiceTest {

    @InjectMocks
    RegisteredVaccineService registeredVaccineService;

    @Mock
    private RegisteredVaccineRepository registeredVaccineRepository;

    @Mock
    private RegisteredVaccineMapper registeredVaccineMapper;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private VaccineRepository vaccineRepository;
    
    RegisteredVaccine registeredVaccine;
    RegisteredVaccineDto registeredVaccineDto;

    @Test
    public void testGetAlRegVaccines() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);
        List<RegisteredVaccineDto> registeredVaccineDtos = new ArrayList<>();
        registeredVaccineDtos.add(registeredVaccineDto);

        //WHEN
        when(registeredVaccineRepository.findAll()).thenReturn(registeredVaccines);
        when(registeredVaccineMapper.mapToRegisteredVaccineDto(registeredVaccine)).thenReturn(registeredVaccineDto);

        //THEN
        List<RegisteredVaccineDto> result = registeredVaccineService.getAllRegisteredVaccines();
        assertEquals(result, registeredVaccineDtos);
    }

    @Test
    public void testDeleteRegVaccine() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();

        //WHEN
        when(registeredVaccineRepository.findById(registeredVaccine.getId())).thenReturn(Optional.ofNullable(registeredVaccine));

        //THEN
        Boolean result = registeredVaccineService.deleteVaccineFromMedicalControl(registeredVaccine.getId());
        assertEquals(result, true);
    }

    @Test
    public void testDeleteRegVaccineException() {
        //GIVEN
        registeredVaccine = null;

        //WHEN
        when(registeredVaccineRepository.findById(2L)).thenReturn(Optional.empty());

        //THEN
        RegisteredVaccineNotFoundException registeredVaccineNotFoundException = assertThrows(RegisteredVaccineNotFoundException.class, () -> registeredVaccineService.deleteVaccineFromMedicalControl(2L));
        assertEquals(String.format(ProjectConstants.REG_VACCINE_NOT_FOUND, 2L), registeredVaccineNotFoundException.getMessage());
    }

    @Test
    public void testUpdateRegistrationDate() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        registeredVaccineDto.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));
        RegisteredVaccine updatedRegisteredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        updatedRegisteredVaccine.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));

        //WHEN
        when(registeredVaccineRepository.findById(registeredVaccine.getId())).thenReturn(Optional.ofNullable(registeredVaccine));
        when(registeredVaccineMapper.mapToRegisteredVaccineDto(updatedRegisteredVaccine)).thenReturn(registeredVaccineDto);
        when(registeredVaccineRepository.save(updatedRegisteredVaccine)).thenReturn(updatedRegisteredVaccine);

        //THEN
        RegisteredVaccineDto result = registeredVaccineService.updateRegistrationDate(registeredVaccine.getId(), new Date(2023, Calendar.NOVEMBER,10));
        assertEquals(result, registeredVaccineDto);
        assertEquals(result.getRegistrationDate(), new Date(2023, Calendar.NOVEMBER,10));
        assertNotNull(result);
    }

    @Test
    public void testUpdatePlacesInRegisteredVaccineException() {
        //GIVEN
        registeredVaccine = null;
        registeredVaccineDto = null;

        //WHEN
        when(registeredVaccineRepository.findById(2L)).thenReturn(Optional.empty());

        //THEN
        RegisteredVaccineNotFoundException registeredVaccineNotFoundException = assertThrows(RegisteredVaccineNotFoundException.class, () -> registeredVaccineService.updateRegistrationDate(2L, new Date(2023, Calendar.NOVEMBER,10)));
        assertEquals(String.format(ProjectConstants.REG_VACCINE_NOT_FOUND, 2L), registeredVaccineNotFoundException.getMessage());
    }

    @Test
    public void testAddNewRegisteredVaccineMedicalRecordException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(medicalRecordRepository.findById(2L)).thenReturn(Optional.empty());

        //THEN
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> registeredVaccineService.associateVaccinesWithMedicalRecord(new ArrayList<>(),2L,  registeredVaccine.getRegistrationDate()));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, 2L), medicalRecordNotFoundException.getMessage());
    }

    @Test
    public void testAddNewRegisteredVaccineVaccineException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(medicalRecordRepository.findById(registeredVaccine.getMedicalRecord().getId())).thenReturn(Optional.ofNullable(MedicalRecordMocks.mockMedicalRecord()));
        when(vaccineRepository.findByVaccineName(registeredVaccine.getVaccine().getVaccineName())).thenReturn(Optional.empty());

        //THEN
        VaccineNotFoundException vaccineNotFoundException = assertThrows(VaccineNotFoundException.class, () -> registeredVaccineService.associateVaccinesWithMedicalRecord(Arrays.asList(registeredVaccineDto.getVaccineDto()),
                registeredVaccine.getMedicalRecord().getId(),  registeredVaccine.getRegistrationDate()));
        assertEquals(String.format(ProjectConstants.VACCINE_NOT_FOUND, TestConstants.VACCINE_NAME), vaccineNotFoundException.getMessage());
    }


    @Test
    public void testAddNewRegisteredVaccine() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccine.setId(null);
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(medicalRecordRepository.findById(registeredVaccine.getMedicalRecord().getId())).thenReturn(Optional.ofNullable(MedicalRecordMocks.mockMedicalRecord()));
        when(vaccineRepository.findByVaccineName(registeredVaccine.getVaccine().getVaccineName())).thenReturn(Optional.ofNullable(VaccineMocks.mockVaccine()));
        when(registeredVaccineRepository.saveAll(Arrays.asList(registeredVaccine))).thenReturn(Arrays.asList(registeredVaccine));
        when(registeredVaccineMapper.mapToRegisteredVaccineDto(registeredVaccine)).thenReturn(registeredVaccineDto);

        //THEN
        List<RegisteredVaccineDto> result = registeredVaccineService.associateVaccinesWithMedicalRecord(Arrays.asList(registeredVaccineDto.getVaccineDto()),
                registeredVaccine.getMedicalRecord().getId(),  registeredVaccine.getRegistrationDate());
        assertEquals(result, Arrays.asList(registeredVaccineDto));
        assertNotNull(result);
    }

}
