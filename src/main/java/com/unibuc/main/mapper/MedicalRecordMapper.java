package com.unibuc.main.mapper;

import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.entity.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    @Autowired
    private AnimalMapper animalMapper;
    
    @Autowired
    private VaccineMapper vaccineMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public MedicalRecord mapToMedicalRecord(MedicalRecordDto medicalRecordDto) {
        return MedicalRecord.builder()
                .generalHealthState(medicalRecordDto.getGeneralHealthState())
                .generationDate(medicalRecordDto.getGenerationDate())
                .build();
    }

    public MedicalRecordDto mapToMedicalRecordDto(MedicalRecord medicalRecord){
        return MedicalRecordDto.builder()
                .generalHealthState(medicalRecord.getGeneralHealthState())
                .generationDate(medicalRecord.getGenerationDate())
                .animalId(medicalRecord.getAnimal().getId())
                .vetFirstName(medicalRecord.getVet().getPersonDetails().getFirstName())
                .vetLastName(medicalRecord.getVet().getPersonDetails().getLastName())
                .build();
    }
}
