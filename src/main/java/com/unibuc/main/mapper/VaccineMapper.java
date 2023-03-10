package com.unibuc.main.mapper;

import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.Vaccine;
import org.springframework.stereotype.Component;

@Component
public class VaccineMapper {

    public Vaccine mapToVaccine(VaccineDto vaccineDto) {
        return Vaccine.builder()
                .vaccineName(vaccineDto.getVaccineName())
                .expirationDate(vaccineDto.getExpirationDate())
                .quantityOnStock(vaccineDto.getQuantityOnStock()).build();
    }

    public VaccineDto mapToVaccineDto(Vaccine vaccine){
        return VaccineDto.builder()
                .vaccineName(vaccine.getVaccineName())
                .expirationDate(vaccine.getExpirationDate())
                .quantityOnStock(vaccine.getQuantityOnStock()).build();
    }

    public Vaccine mapPartialToVaccine(PartialVaccineDto vaccineDto) {
        return Vaccine.builder()
                .expirationDate(vaccineDto.getExpirationDate())
                .quantityOnStock(vaccineDto.getQuantityOnStock()).build();
    }

    public PartialVaccineDto mapToPartialVaccineDto(Vaccine vaccine){
        return PartialVaccineDto.builder()
                .expirationDate(vaccine.getExpirationDate())
                .quantityOnStock(vaccine.getQuantityOnStock()).build();
    }
}
