package com.unibuc.main.mapper;

import com.unibuc.main.dto.CageDto;
import com.unibuc.main.entity.Cage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CageMapper {

    @Autowired
    private CaretakerMapper caretakerMapper;

    public Cage mapToCage(CageDto cageDto) {
        return Cage.builder()
                .id(cageDto.getId())
                .numberPlaces(cageDto.getNumberPlaces())
                .caretaker(caretakerMapper.mapToCaretaker(cageDto.getCaretakerDto()))
                .build();
    }

    public CageDto mapToCageDto(Cage cage){
        return CageDto.builder()
                .id(cage.getId())
                .numberPlaces(cage.getNumberPlaces())
                .caretakerDto(caretakerMapper.mapToCaretakerDto(cage.getCaretaker()))
                .build();
    }
}
