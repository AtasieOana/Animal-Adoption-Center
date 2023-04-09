package com.unibuc.main.utils;

import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.entity.RegisteredVaccine;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class RegisteredVaccineMocks {

    public static RegisteredVaccine mockRegisteredVaccine() {
        return RegisteredVaccine.builder()
                .id(1L)
                .vaccine(VaccineMocks.mockVaccine())
                .medicalRecord(MedicalRecordMocks.mockMedicalRecord())
                .registrationDate(new Date(2022,Calendar.NOVEMBER,10))
                .build();
    }

    public static RegisteredVaccineDto mockRegisteredVaccineDto() {
        return RegisteredVaccineDto.builder()
                .vaccinesDto(Collections.singletonList(VaccineMocks.mockVaccineDto()))
                .medicalRecordDto(MedicalRecordMocks.mockMedicalRecordDto())
                .registrationDate(new Date(2022,Calendar.NOVEMBER,10))
                .build();
    }

    public static PartialRegisteredVaccineDto mockPartialRegisteredVaccineDto() {
        return PartialRegisteredVaccineDto.builder()
                .vaccinesId(Collections.singletonList(VaccineMocks.mockVaccine().getId()))
                .medicalRecordId(MedicalRecordMocks.mockMedicalRecord().getId())
                .registrationDate(new Date(2022,Calendar.NOVEMBER,10))
                .build();
    }
}
