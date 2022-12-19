package com.unibuc.main.mapper;

import com.unibuc.main.dto.CaretakerDto;
import com.unibuc.main.entity.Caretaker;
import com.unibuc.main.entity.EmployeeDetails;
import org.springframework.stereotype.Component;

@Component
public class CaretakerMapper {

    public Caretaker mapToCaretaker(CaretakerDto caretakerDto) {
        return Caretaker.builder()
                .employeeDetails(EmployeeDetails.builder().firstName(caretakerDto.getFirstName())
                                .lastName(caretakerDto.getLastName()).phoneNumber(caretakerDto.getPhoneNumber())
                                .employmentDate(caretakerDto.getEmploymentDate()).build())
                .responsibility(caretakerDto.getResponsibility()).build();
    }

    public CaretakerDto mapToCaretakerDto(Caretaker caretaker){
        return CaretakerDto.builder()
                .firstName(caretaker.getEmployeeDetails().getFirstName())
                .lastName(caretaker.getEmployeeDetails().getLastName())
                .employmentDate(caretaker.getEmployeeDetails().getEmploymentDate())
                .phoneNumber(caretaker.getEmployeeDetails().getPhoneNumber())
                .responsibility(caretaker.getResponsibility()).build();
    }
}
