package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.entity.*;
import com.unibuc.main.exception.*;
import com.unibuc.main.mapper.AnimalMapper;
import com.unibuc.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private AnimalMapper animalMapper;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CageRepository cageRepository;
    @Autowired
    private DietRepository dietRepository;

    public List<AnimalDto> getAllAnimals() {
        return animalRepository.findAll()
                .stream().map(a -> animalMapper.mapToAnimalDto(a))
                .collect(Collectors.toList());
    }

    public List<AnimalDto> showAnimalsInCage(Long cageId){
        return animalRepository.findAllByCage_Id(cageId)
                .stream().map(a -> animalMapper.mapToAnimalDto(a))
                .collect(Collectors.toList());
    }

    public AnimalDto addAnimal(PartialAnimalDto partialAnimalDto) {
        Optional<Diet> diet = dietRepository.findByDietType(partialAnimalDto.getDietType());
        if (diet.isEmpty()) {
            throw new DietNotFoundException(String.format(ProjectConstants.DIET_NOT_FOUND, partialAnimalDto.getDietType()));
        }
        Animal animal = animalMapper.mapPartialToAnimal(partialAnimalDto);
        animal.setDiet(diet.get());
        return animalMapper.mapToAnimalDto(animalRepository.save(animal));
    }

    public AnimalDto adoptAnimal(Long id, String firstName, String lastName){
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isEmpty()) {
            throw new AnimalNotFoundException(String.format(ProjectConstants.ANIMAL_NOT_FOUND, id));
        }
        Animal addedAnimal = animal.get();
        if(addedAnimal.getClient() != null){
            throw new AnimalAlreadyAdoptedException(String.format(ProjectConstants.ANIMAL_ADOPTED, id));
        }
        Optional<Client> client = clientRepository.findClientByName(firstName, lastName);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(String.format(ProjectConstants.CLIENT_NOT_FOUND, firstName + ' ' + lastName));
        }
        addedAnimal.setClient(client.get());
        addedAnimal.setCage(null);
        return animalMapper.mapToAnimalDto(animalRepository.save(addedAnimal));
    }

    public AnimalDto putAnimalInCage(Long id, Long cageId){
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isEmpty()) {
            throw new AnimalNotFoundException(String.format(ProjectConstants.ANIMAL_NOT_FOUND, id));
        }
        Animal addedAnimal = animal.get();
        Optional<Cage> cage = cageRepository.findById(cageId);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        if(showAnimalsInCage(cageId).size() >= cage.get().getNumberPlaces()){
            throw new NoPlaceInCageException(String.format(ProjectConstants.CAGE_FULL, id));
        }
        addedAnimal.setCage(cage.get());
        return animalMapper.mapToAnimalDto(animalRepository.save(addedAnimal));
    }

    public String deleteAdoptedAnimals() {
        List<Animal> animalsAdopted = animalRepository.findAllByClientIsNotNull();
        if(animalsAdopted.isEmpty()){
            return ProjectConstants.NO_ADOPTED_ANIMALS;
        }
        animalRepository.deleteAll(animalsAdopted);
        return ProjectConstants.DELETED_ADOPTED_ANIMALS;
    }

    public AnimalDto getOldestAnimalInCenter() {
        return animalRepository.findOldestAnimal()
                .stream().map(a -> animalMapper.mapToAnimalDto(a))
                .findFirst().orElseThrow(() -> new AnimalNotFoundException(ProjectConstants.ANIMAL_EMPTY));
    }
}
