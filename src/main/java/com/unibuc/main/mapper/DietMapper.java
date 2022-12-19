package com.unibuc.main.mapper;

import com.unibuc.main.dto.DietDto;
import com.unibuc.main.entity.Diet;
import org.springframework.stereotype.Component;

@Component
public class DietMapper {

    public Diet mapToDiet(DietDto dietDto) {
        return Diet.builder()
                .dietType(dietDto.getDietType())
                .quantityOnStock(dietDto.getQuantityOnStock()).build();
    }

    public DietDto mapToDietDto(Diet diet){
        return DietDto.builder()
                .dietType(diet.getDietType())
                .quantityOnStock(diet.getQuantityOnStock()).build();
    }
}
