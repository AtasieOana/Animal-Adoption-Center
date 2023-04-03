package com.unibuc.main.mapper;

import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.entity.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    public MedicalRecord mapToMedicalRecord(MedicalRecordDto medicalRecordDto) {
        return MedicalRecord.builder()
                .id(medicalRecordDto.getId())
                .generalHealthState(medicalRecordDto.getGeneralHealthState())
                .generationDate(medicalRecordDto.getGenerationDate())
                .build();
    }

    public MedicalRecordDto mapToMedicalRecordDto(MedicalRecord medicalRecord){
        return MedicalRecordDto.builder()
                .id(medicalRecord.getId())
                .generalHealthState(medicalRecord.getGeneralHealthState())
                .generationDate(medicalRecord.getGenerationDate())
                .animalId(medicalRecord.getAnimal().getId())
                .vetFirstName(medicalRecord.getVet().getPersonDetails().getFirstName())
                .vetLastName(medicalRecord.getVet().getPersonDetails().getLastName())
                .build();
    }

    public MedicalRecord mapToMedicalRecord(AddMedicalRecordDto addMedicalRecordDto) {
        return MedicalRecord.builder()
                .generalHealthState(addMedicalRecordDto.getGeneralHealthState())
                .generationDate(addMedicalRecordDto.getGenerationDate())
                .build();
    }
}
