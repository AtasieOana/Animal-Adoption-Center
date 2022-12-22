package com.unibuc.main.mapper;

import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Cage;
import com.unibuc.main.repository.DietRepository;
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

    public Animal mapToAnimal(AnimalDto animalDto) {
        Animal animal = Animal.builder()
                .id(animalDto.getId())
                .animalType(animalDto.getAnimalType())
                .birthYear(animalDto.getBirthYear())
                .foundDate(animalDto.getFoundDate())
                .diet(dietMapper.mapToDiet(animalDto.getDietDto()))
                .build();
        if(animalDto.getCageDto() != null) {
            animal.setCage(cageMapper.mapToCage((animalDto.getCageDto())));
        }
        if(animalDto.getClientDto() != null) {
            animal.setClient(clientMapper.mapToClient(animalDto.getClientDto()));
        }
        return animal;
    }

    public AnimalDto mapToAnimalDto(Animal animal){
        AnimalDto animalDto = AnimalDto.builder()
                .id(animal.getId())
                .animalType(animal.getAnimalType())
                .birthYear(animal.getBirthYear())
                .foundDate(animal.getFoundDate())
                .dietDto(dietMapper.mapToDietDto(animal.getDiet()))
                .build();
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

    public PartialAnimalDto mapToPartialAnimalDto(Animal animal){
        return PartialAnimalDto.builder()
                .animalType(animal.getAnimalType())
                .birthYear(animal.getBirthYear())
                .foundDate(animal.getFoundDate())
                .dietType(animal.getDiet().getDietType())
                .build();
    }
}
