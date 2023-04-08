package com.unibuc.main.mapper;

import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.entity.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    public MedicalRecordDto mapToMedicalRecordDto(MedicalRecord medicalRecord){
        MedicalRecordDto medicalRecordDto =  MedicalRecordDto.builder()
                .id(medicalRecord.getId())
                .generalHealthState(medicalRecord.getGeneralHealthState())
                .generationDate(medicalRecord.getGenerationDate())
                .animalId(medicalRecord.getAnimal().getId())
                .build();

        if(medicalRecord.getVet() != null){
            medicalRecordDto.setVetLastName(medicalRecord.getVet().getPersonDetails().getLastName());
            medicalRecordDto.setVetFirstName(medicalRecord.getVet().getPersonDetails().getFirstName());
        }

        return medicalRecordDto;
    }

    public MedicalRecord mapToMedicalRecord(AddMedicalRecordDto addMedicalRecordDto) {
        return MedicalRecord.builder()
                .generalHealthState(addMedicalRecordDto.getGeneralHealthState())
                .generationDate(addMedicalRecordDto.getGenerationDate())
                .build();
    }
}
