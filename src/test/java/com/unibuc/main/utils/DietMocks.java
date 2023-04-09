package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.entity.Diet;

public class DietMocks {

    public static Diet mockDiet() {
        return Diet.builder()
                .id(1L)
                .dietType(TestConstants.DIET_NAME)
                .quantityOnStock(6)
                .build();
    }

    public static DietDto mockDietDto() {
        return DietDto.builder()
                .dietType(TestConstants.DIET_NAME)
                .quantityOnStock(6)
                .build();
    }

}
