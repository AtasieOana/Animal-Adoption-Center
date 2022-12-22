package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.entity.Animal;

import java.util.Calendar;
import java.util.Date;

public class AnimalMocks {


    public static Animal mockAnimal() {
        return Animal.builder()
                .id(1L)
                .animalType(TestConstants.ANIMAL_TYPE)
                .birthYear(2020)
                .foundDate(new Date(2022, Calendar.NOVEMBER,21))
                .diet(DietMocks.mockDiet())
                .build();
    }

    public static AnimalDto mockAnimalDto() {
        return AnimalDto.builder()
                .id(1L)
                .animalType(TestConstants.ANIMAL_TYPE)
                .birthYear(2020)
                .foundDate(new Date(2022,Calendar.NOVEMBER,21))
                .dietDto(DietMocks.mockDietDto())
                .build();
    }

    public static PartialAnimalDto mockPartialAnimalDto() {
        return PartialAnimalDto.builder()
                .animalType(TestConstants.ANIMAL_TYPE)
                .birthYear(2020)
                .foundDate(new Date(2022, Calendar.NOVEMBER,21))
                .dietType(TestConstants.DIET_NAME)
                .build();
    }

    public static AnimalDto mockAnimalDtoWithCage() {
        return AnimalDto.builder()
                .id(1L)
                .animalType(TestConstants.ANIMAL_TYPE)
                .birthYear(2020)
                .foundDate(new Date(2022, Calendar.NOVEMBER,21))
                .dietDto(DietMocks.mockDietDto())
                .cageDto(CageMocks.mockCageDto())
                .build();
    }

}
