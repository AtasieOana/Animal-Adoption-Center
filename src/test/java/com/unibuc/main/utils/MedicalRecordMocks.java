package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.EditMedicalRecordDto;
import com.unibuc.main.entity.MedicalRecord;

import java.util.Calendar;
import java.util.Date;

public class MedicalRecordMocks {

    public static MedicalRecord mockMedicalRecord() {
        return MedicalRecord.builder()
                .id(1L)
                .generalHealthState(TestConstants.GENERAL_HEALTH_DATE)
                .generationDate(new Date(2021, Calendar.APRIL,6))
                .animal(AnimalMocks.mockAnimal())
                .vet(EmployeeMocks.mockVet())
                .build();
    }

    public static MedicalRecordDto mockMedicalRecordDto() {
        return MedicalRecordDto.builder()
                .generalHealthState(TestConstants.GENERAL_HEALTH_DATE)
                .generationDate(new Date(2021, Calendar.APRIL,6))
                .animalId(AnimalMocks.mockAnimal().getId())
                .vetFirstName(EmployeeMocks.mockVet().getPersonDetails().getFirstName())
                .vetLastName(EmployeeMocks.mockVet().getPersonDetails().getLastName())
                .build();
    }

    public static AddMedicalRecordDto mockAddMedicalRecordDto() {
        return AddMedicalRecordDto.builder()
                .generalHealthState(TestConstants.GENERAL_HEALTH_DATE)
                .generationDate(new Date(2021, Calendar.APRIL,6))
                .animalId(AnimalMocks.mockAnimal().getId())
                .vetId(EmployeeMocks.mockVet().getId())
                .build();
    }

    public static EditMedicalRecordDto mockEditMedicalRecordDto() {
        return EditMedicalRecordDto.builder()
                .generalHealthState(TestConstants.GENERAL_HEALTH_DATE)
                .generationDate(new Date(2021, Calendar.APRIL,6))
                .build();
    }

    public static MedicalRecord mockMedicalRecord2() {
        return MedicalRecord.builder()
                .id(2L)
                .generalHealthState(TestConstants.GENERAL_HEALTH_DATE)
                .generationDate(new Date(2021, Calendar.APRIL,6))
                .animal(AnimalMocks.mockAnimal2())
                .vet(EmployeeMocks.mockVet())
                .build();
    }

}
