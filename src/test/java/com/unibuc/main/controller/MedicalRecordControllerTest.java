package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.service.MedicalRecordService;
import com.unibuc.main.utils.MedicalRecordMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {
    @InjectMocks
    MedicalRecordController medicalRecordController;
    @Mock
    MedicalRecordService medicalRecordService;
    MedicalRecordDto medicalRecordDto;
    PartialMedicalRecordDto partialMedicalRecordDto;

    @Test
    public void getMedicalRecordsForAnimalTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);

        //WHEN
        when(medicalRecordService.getAllMedicalRecordsForAnimal(medicalRecordDto.getAnimalId())).thenReturn(medicalRecordDtos);

        //THEN
        ResponseEntity<List<MedicalRecordDto>> result = medicalRecordController.getAllMedicalRecordsForAnimal(medicalRecordDto.getAnimalId());
        assertEquals(result.getBody(), medicalRecordDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }
    @Test
    public void addNewMedicalRecordTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();

        //WHEN
        when(medicalRecordService.addMedicalRecord(medicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        ResponseEntity<MedicalRecordDto> result = medicalRecordController.addNewMedicalRecord(medicalRecordDto);
        assertEquals(result.getBody(), medicalRecordDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteMedicalRecordBeforeADateTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when( medicalRecordService.deleteMedicalRecordBeforeADate("2021-10-10")).thenReturn(ProjectConstants.DELETED_RECORDS);

        //THEN
        ResponseEntity<String> result = medicalRecordController.deleteMedicalRecordBeforeADate("2021-10-10");
        assertEquals(result.getBody(), ProjectConstants.DELETED_RECORDS);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateMedicalRecordTest() {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();
        partialMedicalRecordDto = MedicalRecordMocks.mockPartialMedicalRecordDto();
        medicalRecordDto.setGeneralHealthState("Very good");
        partialMedicalRecordDto.setGeneralHealthState("Very good");

        //WHEN
        when(medicalRecordService.updateMedicalRecord(1L, partialMedicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        ResponseEntity<MedicalRecordDto> result = medicalRecordController.updateMedicalRecord(1L, partialMedicalRecordDto);
        assertEquals(result.getBody(), medicalRecordDto);
        assertEquals(result.getStatusCode().value(), 200);
    }
}
