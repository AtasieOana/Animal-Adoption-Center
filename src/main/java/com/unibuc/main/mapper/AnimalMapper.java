package com.unibuc.main.mapper;

import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.entity.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    @Autowired
    private ClientMapper clientMapper;
    
    @Autowired
    private CageMapper cageMapper;

    @Autowired
    private DietMapper dietMapper;

    public AnimalDto mapToAnimalDto(Animal animal){
        AnimalDto animalDto = AnimalDto.builder()
                .id(animal.getId())
                .animalType(animal.getAnimalType())
                .birthYear(animal.getBirthYear())
                .foundDate(animal.getFoundDate())
                .build();
        if(animal.getDiet() != null) {
            animalDto.setDietDto(dietMapper.mapToDietDto(animal.getDiet()));
        }
        if(animal.getCage() != null) {
            animalDto.setCageDto(cageMapper.mapToCageDto((animal.getCage())));
        }
        if(animal.getClient() != null) {
            animalDto.setClientDto(clientMapper.mapToClientDto(animal.getClient()));
        }
        return animalDto;
    }

    public Animal mapPartialToAnimal(PartialAnimalDto animalDto) {
        return Animal.builder()
                .animalType(animalDto.getAnimalType())
                .birthYear(animalDto.getBirthYear())
                .foundDate(animalDto.getFoundDate())
                .build();
    }

}
