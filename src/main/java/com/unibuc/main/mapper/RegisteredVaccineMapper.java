package com.unibuc.main.mapper;

import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.entity.RegisteredVaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisteredVaccineMapper {

    @Autowired
    private VaccineMapper vaccineMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    public RegisteredVaccine mapToRegisteredVaccine(RegisteredVaccineDto registeredVaccineDto) {
        return RegisteredVaccine.builder()
                .id(registeredVaccineDto.getId())
                .vaccine(vaccineMapper.mapToVaccine(registeredVaccineDto.getVaccineDto()))
                .medicalRecord(medicalRecordMapper.mapToMedicalRecord(registeredVaccineDto.getMedicalRecordDto()))
                .registrationDate(registeredVaccineDto.getRegistrationDate())
                .build();
    }

    public RegisteredVaccineDto mapToRegisteredVaccineDto(RegisteredVaccine registeredVaccine) {
        return RegisteredVaccineDto.builder()
                .id(registeredVaccine.getId())
                .vaccineDto(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine()))
                .medicalRecordDto(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccine.getMedicalRecord()))
                .registrationDate(registeredVaccine.getRegistrationDate())
                .build();
    }
}
