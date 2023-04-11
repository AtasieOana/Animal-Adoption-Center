package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.AdoptAnimalDto;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.AddAnimalDto;
import com.unibuc.main.entity.Animal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnimalMocks {

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Animal mockAnimal() {
        String date_string = "26-09-2022";
        try {
            Date date = formatter.parse(date_string);
            return Animal.builder()
                    .id(1L)
                    .animalType(TestConstants.ANIMAL_TYPE)
                    .birthYear(2020)
                    .foundDate(date)
                    .diet(DietMocks.mockDiet())
                    .cage(CageMocks.mockCage())
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static AnimalDto mockAnimalDto() {
        String date_string = "26-09-2022";
        try {
            Date date = formatter.parse(date_string);
            return AnimalDto.builder()
                    .id(1L)
                    .animalType(TestConstants.ANIMAL_TYPE)
                    .birthYear(2020)
                    .foundDate(date)
                    .dietDto(DietMocks.mockDietDto())
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static AddAnimalDto mockAddAnimalDto() {
        String date_string = "26-09-2022";
        try {
            Date date = formatter.parse(date_string);
            return AddAnimalDto.builder()
                    .animalType(TestConstants.ANIMAL_TYPE)
                    .birthYear(2020)
                    .foundDate(date)
                    .dietType(TestConstants.DIET_NAME)
                    .cageId(1L)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Animal mockAnimal2() {
        String date_string = "22-10-2023";
        try {
            Date date = formatter.parse(date_string);
            return Animal.builder()
                    .id(2L)
                    .animalType(TestConstants.ANIMAL_TYPE)
                    .birthYear(2020)
                    .foundDate(date)
                    .diet(DietMocks.mockDiet())
                    .cage(CageMocks.mockCage())
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Animal mockAnimalWithClient() {
        String date_string = "22-10-2022";
        try {
            Date date = formatter.parse(date_string);
            return Animal.builder()
                    .id(3L)
                    .animalType(TestConstants.ANIMAL_TYPE)
                    .birthYear(2020)
                    .foundDate(date)
                    .diet(DietMocks.mockDiet())
                    .client(ClientMocks.mockClient())
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static AdoptAnimalDto mockAdoptAnimalDto() {
        return AdoptAnimalDto.builder()
                .id(1L)
                .clientId(1L)
                .build();
    }
}
