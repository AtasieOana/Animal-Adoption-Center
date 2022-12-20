package com.unibuc.main.mapper;

import com.unibuc.main.dto.CageDto;
import com.unibuc.main.entity.Cage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CageMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Cage mapToCage(CageDto cageDto) {
        return Cage.builder()
                .id(cageDto.getId())
                .numberPlaces(cageDto.getNumberPlaces())
                .caretaker(employeeMapper.mapToEmployee(cageDto.getCaretakerDto()))
                .build();
    }

    public CageDto mapToCageDto(Cage cage){
        return CageDto.builder()
                .id(cage.getId())
                .numberPlaces(cage.getNumberPlaces())
                .caretakerDto(employeeMapper.mapToEmployeeDto(cage.getCaretaker()))
                .build();
    }
}
