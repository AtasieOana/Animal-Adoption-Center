package com.unibuc.main.mapper;

import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.entity.Cage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CageMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Cage mapToCage(CageDto cageDto) {
        if(cageDto.getCaretaker() != null) {
            return Cage.builder()
                    .id(cageDto.getId())
                    .numberPlaces(cageDto.getNumberPlaces())
                    .caretaker(employeeMapper.mapToEmployee(cageDto.getCaretaker()))
                    .build();
        }
        else{
            return Cage.builder()
                    .id(cageDto.getId())
                    .numberPlaces(cageDto.getNumberPlaces())
                    .build();
        }
    }

    public CageDto mapToCageDto(Cage cage){
        if(cage.getCaretaker() != null){
            return CageDto.builder()
                    .id(cage.getId())
                    .numberPlaces(cage.getNumberPlaces())
                    .caretaker(employeeMapper.mapToEmployeeDto(cage.getCaretaker()))
                    .build();
        }
        else{
            return CageDto.builder()
                    .id(cage.getId())
                    .numberPlaces(cage.getNumberPlaces())
                    .build();
        }

    }

    public Cage mapPartialToCage(PartialCageDto cageDto) {
        if(cageDto.getCaretaker() != null) {
            return Cage.builder()
                    .numberPlaces(cageDto.getNumberPlaces())
                    .caretaker(employeeMapper.mapToEmployee(cageDto.getCaretaker()))
                    .build();
        }
        else{
            return Cage.builder()
                    .numberPlaces(cageDto.getNumberPlaces())
                    .build();
        }
    }

}
