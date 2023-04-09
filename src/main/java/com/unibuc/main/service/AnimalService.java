package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.AddAnimalDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Cage;
import com.unibuc.main.entity.Client;
import com.unibuc.main.entity.Diet;
import com.unibuc.main.exception.*;
import com.unibuc.main.mapper.AnimalMapper;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.CageRepository;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Autowired
    private MedicalRecordService medicalRecordService;

    public List<AnimalDto> getAllAnimals() {
        return animalRepository.findAll()
                .stream().map(a -> animalMapper.mapToAnimalDto(a))
                .collect(Collectors.toList());
    }

    public AnimalDto getAnimalById(Long id) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isEmpty()) {
            throw new AnimalNotFoundException(String.format(ProjectConstants.ANIMAL_NOT_FOUND, id));
        }
        return animalMapper.mapToAnimalDto(animal.get());
    }

    public AnimalDto getOldestAnimalInCenter() {
        return animalRepository.findOldestAnimal()
                .stream().map(a -> animalMapper.mapToAnimalDto(a))
                .findFirst().orElseThrow(() -> new AnimalNotFoundException(ProjectConstants.ANIMAL_EMPTY));
    }

    public AnimalDto addAnimal(AddAnimalDto addAnimalDto) {
        Optional<Diet> diet = dietRepository.findByDietType(addAnimalDto.getDietType());
        if (diet.isEmpty()) {
            throw new DietNotFoundException(String.format(ProjectConstants.DIET_NOT_FOUND, addAnimalDto.getDietType()));
        }
        Animal animal = animalMapper.mapPartialToAnimal(addAnimalDto);
        animal.setDiet(diet.get());
        Optional<Cage> cage = cageRepository.findById(addAnimalDto.getCageId());
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, addAnimalDto.getCageId()));
        }
        animal.setCage(cage.get());
        if(animalRepository.findAllByCageId(addAnimalDto.getCageId()).size() >= cage.get().getNumberPlaces()){
            throw new NoPlaceInCageException(String.format(ProjectConstants.CAGE_FULL, addAnimalDto.getCageId()));
        }
        return animalMapper.mapToAnimalDto(animalRepository.save(animal));
    }

    public AnimalDto adoptAnimal(Long id, Long clientId){
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isEmpty()) {
            throw new AnimalNotFoundException(String.format(ProjectConstants.ANIMAL_NOT_FOUND, id));
        }
        Animal addedAnimal = animal.get();
        if(addedAnimal.getClient() != null){
            throw new AnimalAlreadyAdoptedException(String.format(ProjectConstants.ANIMAL_ADOPTED, id));
        }
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(String.format(ProjectConstants.CLIENT_NOT_ID, clientId));
        }
        addedAnimal.setClient(client.get());
        addedAnimal.setCage(null);
        return animalMapper.mapToAnimalDto(animalRepository.save(addedAnimal));
    }

    public String deleteAdoptedAnimals() {
        List<Animal> animalsAdopted = animalRepository.findAllByClientIsNotNull();
        if(animalsAdopted.isEmpty()){
            return ProjectConstants.NO_ADOPTED_ANIMALS;
        }
        medicalRecordService.deleteMedicalRecordAnimals(animalsAdopted);
        animalRepository.deleteAll(animalsAdopted);
        return ProjectConstants.DELETED_ADOPTED_ANIMALS;
    }

    public Page<AnimalDto> findPaginatedAnimals(Pageable pageable) {
        Page<AnimalDto> animalPage = animalRepository.findAll(pageable).map(animal -> animalMapper.mapToAnimalDto(animal));
        return animalPage;
    }
}
