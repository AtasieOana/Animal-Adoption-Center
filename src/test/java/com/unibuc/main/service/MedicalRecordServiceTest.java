package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.EditMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.exception.AnimalNotFoundException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.mapper.MedicalRecordMapper;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.utils.MedicalRecordMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
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

    MedicalRecord medicalRecord;
    MedicalRecordDto medicalRecordDto;
    EditMedicalRecordDto editMedicalRecordDto;
    AddMedicalRecordDto addMedicalRecordDto;

    @Test
    public void testAddNewMedicalRecord() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(addMedicalRecordDto)).thenReturn(medicalRecord);
        when( animalRepository.findById(addMedicalRecordDto.getAnimalId())).thenReturn(Optional.ofNullable(medicalRecord.getAnimal()));
        when(employeeRepository.findById(addMedicalRecordDto.getVetId())).thenReturn(Optional.ofNullable(medicalRecord.getVet()));
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(medicalRecord);
        when(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord)).thenReturn(medicalRecordDto);

        //THEN
        MedicalRecordDto result = medicalRecordService.addMedicalRecord(addMedicalRecordDto);
        assertEquals(result, medicalRecordDto);
        assertThat(result.getVetLastName()).isNotNull();
        assertThat(result.getAnimalId()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewMedicalRecordAnimalException() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(addMedicalRecordDto)).thenReturn(medicalRecord);
        when(animalRepository.findById(medicalRecordDto.getAnimalId())).thenReturn(Optional.empty());

        //THEN
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> medicalRecordService.addMedicalRecord(addMedicalRecordDto));
        assertEquals(String.format(ProjectConstants.ANIMAL_NOT_FOUND, medicalRecordDto.getAnimalId()), animalNotFoundException.getMessage());
    }

    @Test
    public void testAddNewMedicalRecordVetException() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        addMedicalRecordDto = MedicalRecordMocks.mockAddMedicalRecordDto();

        //WHEN
        when(medicalRecordMapper.mapToMedicalRecord(addMedicalRecordDto)).thenReturn(medicalRecord);
        when( animalRepository.findById(addMedicalRecordDto.getAnimalId())).thenReturn(Optional.ofNullable(medicalRecord.getAnimal()));
        when(employeeRepository.findById(addMedicalRecordDto.getVetId())).thenReturn(Optional.empty());

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> medicalRecordService.addMedicalRecord(addMedicalRecordDto));
        assertEquals(String.format(ProjectConstants.EMP_ID_NOT_FOUND, addMedicalRecordDto.getVetId()), employeeNotFoundException.getMessage());
    }

    @Test
    public void testGetAllMedicalRecords() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(medicalRecord);
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);

        //WHEN
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);
        when(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord)).thenReturn(medicalRecordDto);

        //THEN
        List<MedicalRecordDto> result = medicalRecordService.getAllMedicalRecords();
        assertEquals(result, medicalRecordDtos);
    }

    @Test
    public void testGetMedicalRecordById() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.ofNullable(medicalRecord));
        when(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord)).thenReturn(medicalRecordDto);

        //THEN
        MedicalRecordDto result = medicalRecordService.getMedicalRecordById(medicalRecord.getId());
        assertEquals(result, medicalRecordDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void testGetMedicalRecordByIdException() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.empty());

        //THEN
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordService.getMedicalRecordById(medicalRecord.getId()));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, medicalRecord.getId()), medicalRecordNotFoundException.getMessage());
    }
    
    @Test
    public void testDeleteMedicalById() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.ofNullable(medicalRecord));

        //THEN
        Boolean result = medicalRecordService.deleteMedicalRecord(medicalRecord.getId());
        assertEquals(result, true);
    }

    @Test
    public void testDeleteMedicalByIdException() throws ParseException {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.empty());

        //THEN
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordService.deleteMedicalRecord(medicalRecord.getId()));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, medicalRecord.getId()), medicalRecordNotFoundException.getMessage());
    }


    @Test
    public void testUpdateMedicalRecord() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        medicalRecordDto.setGeneralHealthState("Bad");

        editMedicalRecordDto = MedicalRecordMocks.mockEditMedicalRecordDto();
        editMedicalRecordDto.setGeneralHealthState("Bad");

        MedicalRecord updatedMedicalRecord = MedicalRecordMocks.mockMedicalRecord();
        updatedMedicalRecord.setGeneralHealthState("Bad");

        //WHEN
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.ofNullable(medicalRecord));
        when(medicalRecordMapper.mapToMedicalRecordDto(updatedMedicalRecord)).thenReturn(medicalRecordDto);
        when(medicalRecordRepository.save(updatedMedicalRecord)).thenReturn(updatedMedicalRecord);

        //THEN
        MedicalRecordDto result = medicalRecordService.updateMedicalRecord(medicalRecord.getId(), editMedicalRecordDto);
        assertEquals(result, medicalRecordDto);
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
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> medicalRecordService.updateMedicalRecord(2L, editMedicalRecordDto));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, 2L), medicalRecordNotFoundException.getMessage());
    }

    @Test
    public void testFindPaginatedMedicalRecords() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();
        Pageable pageable = PageRequest.of(0,20);

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);

        //WHEN
        when(medicalRecordRepository.findAll(pageable)).thenReturn(new PageImpl<>(medicalRecordList));
        when(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord)).thenReturn(medicalRecordDto);

        //THEN
        Page<MedicalRecordDto> result = medicalRecordService.findPaginatedMedicalRecords(pageable);
        assertEquals(result, new PageImpl<>(medicalRecordDtos));
    }

    @Test
    public void testDeleteMedicalRecordAnimals() {
        //GIVEN
        medicalRecord = MedicalRecordMocks.mockMedicalRecord();
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);

        //WHEN
        when(medicalRecordRepository.findMedicalRecordsByAnimalsId(Collections.singletonList(medicalRecord.getAnimal().getId()))).thenReturn(medicalRecordList);

        //THEN
        Boolean result = medicalRecordService.deleteMedicalRecordAnimals(Collections.singletonList(medicalRecord.getAnimal()));
        assertEquals(result, true);
    }
}
