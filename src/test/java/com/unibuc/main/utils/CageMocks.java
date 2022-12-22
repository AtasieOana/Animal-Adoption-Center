package com.unibuc.main.utils;

import com.unibuc.main.dto.CageDto;
import com.unibuc.main.entity.Cage;


public class CageMocks {


    public static Cage mockCage() {
        return Cage.builder()
                .id(1L)
                .numberPlaces(3)
                .caretaker(EmployeeMocks.mockCaretaker())
                .build();
    }

    public static CageDto mockCageDto() {
        return CageDto.builder()
                .id(1L)
                .numberPlaces(3)
                .caretaker(EmployeeMocks.mockCaretakerDto())
                .build();
    }

    public static CageDto mockCageDto2() {
        return CageDto.builder()
                .numberPlaces(3)
                .caretaker(EmployeeMocks.mockCaretakerDto2())
                .build();
    }

}
