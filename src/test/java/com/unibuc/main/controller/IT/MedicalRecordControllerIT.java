package com.unibuc.main.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.controller.MedicalRecordController;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.service.MedicalRecordService;
import com.unibuc.main.utils.MedicalRecordMocks;
import com.unibuc.main.utils.VaccineMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    MedicalRecordService medicalRecordService;
    MedicalRecordDto medicalRecordDto;
    PartialMedicalRecordDto partialMedicalRecordDto;

    @Test
    public void getMedicalRecordsForAnimalTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();
        List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
        medicalRecordDtos.add(medicalRecordDto);

        //WHEN
        when(medicalRecordService.getAllMedicalRecordsForAnimal(medicalRecordDto.getAnimalId())).thenReturn(medicalRecordDtos);

        //THEN
        mockMvc.perform(get("/medicalRecords/getMedicalRecordsForAnimal/" + medicalRecordDto.getAnimalId()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(medicalRecordDtos)));
    }

    @Test
    public void getMostUsedVaccineTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when(medicalRecordService.getMostUsedVaccine()).thenReturn(VaccineMocks.mockVaccineDto());

        //THEN
        mockMvc.perform(get("/medicalRecords/getMostUsedVaccine"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(VaccineMocks.mockVaccineDto())));
    }
    @Test
    public void addNewMedicalRecordTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();

        //WHEN
        when(medicalRecordService.addMedicalRecord(medicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        mockMvc.perform(post("/medicalRecords")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medicalRecordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(medicalRecordDto)));
    }

    @Test
    public void deleteMedicalRecordBeforeADateTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto();

        //WHEN
        when( medicalRecordService.deleteMedicalRecordBeforeADate("2021-10-10")).thenReturn(ProjectConstants.DELETED_RECORDS);

        //THEN
        mockMvc.perform(delete("/medicalRecords/deleteMedicalRecordBeforeDate/2021-10-10"))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.DELETED_RECORDS));
    }

    @Test
    public void updateMedicalRecordTest() throws Exception {
        //GIVEN
        medicalRecordDto = MedicalRecordMocks.mockMedicalRecordDto2();
        partialMedicalRecordDto = MedicalRecordMocks.mockPartialMedicalRecordDto();
        medicalRecordDto.setGeneralHealthState("Very good");
        partialMedicalRecordDto.setGeneralHealthState("Very good");

        //WHEN
        when(medicalRecordService.updateMedicalRecord(1L, partialMedicalRecordDto)).thenReturn(medicalRecordDto);

        //THEN
        mockMvc.perform(put("/medicalRecords/1")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(partialMedicalRecordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generalHealthState").value("Very good"));
    }
}
