package com.unibuc.main.mapper;

import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RegisteredVaccineMapper {

    @Autowired
    private VaccineRepository vaccineRepository;

    public PartialRegisteredVaccineDto mapDtoToPartial(RegisteredVaccineDto registeredVaccineDto) {
        return PartialRegisteredVaccineDto.builder()
                .medicalRecordId(registeredVaccineDto.getMedicalRecordDto().getId())
                .vaccinesId(registeredVaccineDto.getVaccinesDto().stream().map(vaccineDto -> vaccineRepository.findByVaccineName(vaccineDto.getVaccineName()).get().getId()).collect(Collectors.toList()))
                .registrationDate(registeredVaccineDto.getRegistrationDate())
                .build();
    }

}
