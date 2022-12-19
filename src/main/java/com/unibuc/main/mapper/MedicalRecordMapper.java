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
    private VetMapper vetMapper;

    public MedicalRecord mapToMedicalRecord(MedicalRecordDto medicalRecordDto) {
        return MedicalRecord.builder()
                .id(medicalRecordDto.getId())
                .generalHealthState(medicalRecordDto.getGeneralHealthState())
                .generationDate(medicalRecordDto.getGenerationDate())
                .animal(animalMapper.mapToAnimal((medicalRecordDto.getAnimalDto())))
                .vaccine(vaccineMapper.mapToVaccine(medicalRecordDto.getVaccineDto()))
                .vet(vetMapper.mapToVet(medicalRecordDto.getVetDto()))
                .build();
    }

    public MedicalRecordDto mapToMedicalRecordDto(MedicalRecord medicalRecord){
        return MedicalRecordDto.builder()
                .id(medicalRecord.getId())
                .generalHealthState(medicalRecord.getGeneralHealthState())
                .generationDate(medicalRecord.getGenerationDate())
                .animalDto(animalMapper.mapToAnimalDto((medicalRecord.getAnimal())))
                .vaccineDto(vaccineMapper.mapToVaccineDto(medicalRecord.getVaccine()))
                .vetDto(vetMapper.mapToVetDto(medicalRecord.getVet()))
                .build();
    }
}
