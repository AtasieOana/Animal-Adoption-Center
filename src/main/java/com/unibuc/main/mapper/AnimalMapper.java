package com.unibuc.main.mapper;

import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Cage;
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
        return Animal.builder()
                .id(animalDto.getId())
                .animalType(animalDto.getAnimalType())
                .birthYear(animalDto.getBirthYear())
                .foundDate(animalDto.getFoundDate())
                .cage(cageMapper.mapToCage((animalDto.getCageDto())))
                .client(clientMapper.mapToClient(animalDto.getClientDto()))
                .diet(dietMapper.mapToDiet(animalDto.getDietDto()))
                .build();
    }

    public AnimalDto mapToAnimalDto(Animal animal){
        return AnimalDto.builder()
                .id(animal.getId())
                .animalType(animal.getAnimalType())
                .birthYear(animal.getBirthYear())
                .foundDate(animal.getFoundDate())
                .cageDto(cageMapper.mapToCageDto((animal.getCage())))
                .clientDto(clientMapper.mapToClientDto(animal.getClient()))
                .dietDto(dietMapper.mapToDietDto(animal.getDiet()))
                .build();
    }
}
