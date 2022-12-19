package com.unibuc.main.mapper;

import com.unibuc.main.dto.VetDto;
import com.unibuc.main.entity.EmployeeDetails;
import com.unibuc.main.entity.Vet;
import org.springframework.stereotype.Component;

@Component
public class VetMapper {

    public Vet mapToVet(VetDto vetDto) {
        return Vet.builder()
                .employeeDetails(EmployeeDetails.builder().firstName(vetDto.getFirstName())
                                .lastName(vetDto.getLastName()).phoneNumber(vetDto.getPhoneNumber())
                                .employmentDate(vetDto.getEmploymentDate()).build())
                .experience(vetDto.getExperience()).build();
    }

    public VetDto mapToVetDto(Vet vet){
        return VetDto.builder()
                .firstName(vet.getEmployeeDetails().getFirstName())
                .lastName(vet.getEmployeeDetails().getLastName())
                .employmentDate(vet.getEmployeeDetails().getEmploymentDate())
                .phoneNumber(vet.getEmployeeDetails().getPhoneNumber())
                .experience(vet.getExperience()).build();
    }
}
