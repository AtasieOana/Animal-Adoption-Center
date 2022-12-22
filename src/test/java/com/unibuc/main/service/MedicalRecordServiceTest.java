package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.exception.AnimalNotFoundException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.MedicalRecordMapper;
import com.unibuc.main.mapper.VaccineMapper;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.utils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @InjectMocks
    MedicalRecordService medicalRecordService;

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @Mock
    MedicalRecordMapper medicalRecordMapper;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    AnimalRepository animalRepository;

    @Mock
    VaccineRepository vaccineRepository;

    @Mock
    VaccineMapper vaccineMapper;

    MedicalRecord medicalRecord;
    MedicalRecordDto medicalRecordDto;
    PartialMedicalRecordDto partialMedicalRecordDto;

    @Test
    public void getAllMedicalRecordsForAnimalTest() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);

        //WHEN
        when(medicalRecordRepository.findAllByAnimal_Id(medicalRecordDto.getAnimalId())).thenReturn(medicalRecordList);
        when(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord)).thenReturn(medicalRecordDto);

        //THEN
        List<MedicalRecordDto> result = medicalRecordService.getAllMedicalRecordsForAnimal(medicalRecordDto.getAnimalId());
        assertEquals(result, medicalRecordDtos);
    }

    @Test
    public void testAddNewMedicalRecord() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        MedicalRecord updatedMedicalRecord = MedicalRecordMocks.mockMedicalRecord2();
        MedicalRecordDto updatedMedicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(medicalRecordDto)).thenReturn(medicalRecord);
        when(animalRepository.findById(medicalRecordDto.getAnimalId())).thenReturn(Optional.ofNullable(AnimalMocks.mockAnimal()));
        when(vaccineRepository.findByVaccineName(medicalRecordDto.getVaccineName())).thenReturn(Optional.ofNullable(VaccineMocks.mockVaccine()));
        when(employeeRepository.findVetByName(medicalRecordDto.getVetFirstName(), medicalRecordDto.getVetLastName())).thenReturn(Optional.ofNullable(EmployeeMocks.mockVet()));
        when(medicalRecordMapper.mapToMedicalRecordDto(updatedMedicalRecord)).thenReturn(updatedMedicalRecordDto);
        when(medicalRecordRepository.save(updatedMedicalRecord)).thenReturn(updatedMedicalRecord);

        //THEN
        MedicalRecordDto result = medicalRecordService.addMedicalRecord(medicalRecordDto);
        assertEquals(result, updatedMedicalRecordDto);
        assertThat(result.getVaccineName()).isNotNull();
        assertThat(result.getVetLastName()).isNotNull();
        assertThat(result.getAnimalId()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewMedicalRecordAnimalException() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord2();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(medicalRecordDto)).thenReturn(medicalRecord);
        when(animalRepository.findById(medicalRecordDto.getAnimalId())).thenReturn(Optional.empty());

        //THEN
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> medicalRecordService.addMedicalRecord(medicalRecordDto));
        assertEquals(String.format(ProjectConstants.ANIMAL_NOT_FOUND, medicalRecordDto.getAnimalId()), animalNotFoundException.getMessage());
    }

    @Test
    public void testAddNewMedicalRecordVaccineException() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord2();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(medicalRecordDto)).thenReturn(medicalRecord);
        when(animalRepository.findById(medicalRecordDto.getAnimalId())).thenReturn(Optional.ofNullable(AnimalMocks.mockAnimal()));
        when(vaccineRepository.findByVaccineName(medicalRecordDto.getVaccineName())).thenReturn(Optional.empty());

        //THEN
        VaccineNotFoundException vaccineNotFoundException = assertThrows(VaccineNotFoundException.class, () -> medicalRecordService.addMedicalRecord(medicalRecordDto));
        assertEquals(String.format(ProjectConstants.VACCINE_NOT_FOUND, TestConstants.VACCINE_NAME), vaccineNotFoundException.getMessage());
    }

    @Test
    public void testAddNewMedicalRecordVetException() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord2();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(medicalRecordDto)).thenReturn(medicalRecord);
        when(animalRepository.findById(medicalRecordDto.getAnimalId())).thenReturn(Optional.ofNullable(AnimalMocks.mockAnimal()));
        when(vaccineRepository.findByVaccineName(medicalRecordDto.getVaccineName())).thenReturn(Optional.ofNullable(VaccineMocks.mockVaccine()));
        when(employeeRepository.findVetByName(medicalRecordDto.getVetFirstName(), medicalRecordDto.getVetLastName())).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> medicalRecordService.addMedicalRecord(medicalRecordDto));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), employeeNotFoundException.getMessage());
    }

    @Test
    public void getMostUsedVaccineTest() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        List<Vaccine> vaccineList = new ArrayList<>();
        vaccineList.add(VaccineMocks.mockVaccine());

        //WHEN
        when(medicalRecordRepository.findAllVaccines()).thenReturn(vaccineList);
        when(vaccineMapper.mapToVaccineDto(VaccineMocks.mockVaccine())).thenReturn(VaccineMocks.mockVaccineDto());

        //THEN
        VaccineDto result = medicalRecordService.getMostUsedVaccine();
        assertEquals(result, VaccineMocks.mockVaccineDto());
    }

    @Test
    public void testDeleteMedicalRecordBeforeADate() throws ParseException {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);

        //WHEN
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        when(medicalRecordRepository.findAllByGenerationDateBefore((simpleDateFormat.parse("2022-01-01")))).thenReturn(medicalRecordList);

        //THEN
        String result = medicalRecordService.deleteMedicalRecordBeforeADate("2022-01-01");
        assertEquals(result, ProjectConstants.DELETED_RECORDS);
    }

    @Test
    public void testDeleteMedicalRecordBeforeADateEmpty() throws ParseException {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        when(medicalRecordRepository.findAllByGenerationDateBefore((simpleDateFormat.parse("2022-01-01")))).thenReturn(new ArrayList<>());

        //THEN
        String result = medicalRecordService.deleteMedicalRecordBeforeADate("2022-01-01");
        assertEquals(result, ProjectConstants.NO_RECORD_BEFORE);
    }


    @Test
    public void testUpdateMedicalRecord() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord2();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();
        medicalRecordDto.setGeneralHealthState("Bad");

        partialMedicalRecordDto = MedicalRecordMocks.mockPartialMedicalRecordDto();
        partialMedicalRecordDto.setGeneralHealthState("Bad");

        MedicalRecord updatedMedicalRecord = MedicalRecordMocks.mockMedicalRecord2();
        updatedMedicalRecord.setGeneralHealthState("Bad");

        //WHEN
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.ofNullable(medicalRecord));
        when(medicalRecordMapper.mapToMedicalRecordDto(updatedMedicalRecord)).thenReturn(medicalRecordDto);
        when(medicalRecordRepository.save(updatedMedicalRecord)).thenReturn(updatedMedicalRecord);

        //THEN
        MedicalRecordDto result = medicalRecordService.updateMedicalRecord(medicalRecord.getId(), partialMedicalRecordDto);
        assertEquals(result, medicalRecordDto);
        assertEquals(result.getVaccineName(), TestConstants.VACCINE_NAME);
        assertEquals(result.getGeneralHealthState(), updatedMedicalRecord.getGeneralHealthState());
        assertNotNull(result);
    }

    @Test
    public void testUpdateMedicalRecordException() {
        //GIVEN
        medicalRecord = null;
        medicalRecordDto = null;

        //WHEN
        when(medicalRecordRepository.findById(2L)).thenReturn(Optional.ofNullable(medicalRecord));

        //THEN
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordService.updateMedicalRecord(2L,partialMedicalRecordDto));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, 2L), medicalRecordNotFoundException.getMessage());}

}
