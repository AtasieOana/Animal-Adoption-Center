package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Cage;
import com.unibuc.main.exception.*;
import com.unibuc.main.mapper.AnimalMapper;
import com.unibuc.main.repository.*;
import com.unibuc.main.utils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @InjectMocks
    AnimalService animalService;

    @Mock
    AnimalRepository animalRepository;

    @Mock
    AnimalMapper animalMapper;

    @Mock
    CageRepository cageRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    DietRepository dietRepository;

    Animal animal;

    AnimalDto animalDto;
    
    PartialAnimalDto partialAnimalDto;

    @Test
    public void testGetAllAnimals() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();

        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);

        //WHEN
        when(animalRepository.findAll()).thenReturn(animals);
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);

        //THEN
        List<AnimalDto> result = animalService.getAllAnimals();
        assertEquals(result, animalDtos);
    }

    
    @Test
    public void testAddAnimal() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        partialAnimalDto = AnimalMocks.mockPartialAnimalDto();

        //WHEN
        when(dietRepository.findByDietType(partialAnimalDto.getDietType())).thenReturn(Optional.ofNullable(DietMocks.mockDiet()));
        when(animalMapper.mapPartialToAnimal(partialAnimalDto)).thenReturn(animal);
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);
        when(animalRepository.save(animal)).thenReturn(animal);

        //THEN
        AnimalDto result = animalService.addAnimal(partialAnimalDto);
        assertEquals(result, animalDto);
        assertEquals(result.getDietDto(), DietMocks.mockDietDto());
        assertThat(result.getClientDto()).isNull();
        assertThat(result.getCageDto()).isNull();
        assertNotNull(result);
    }

    @Test
    public void testAddAnimalException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        partialAnimalDto = AnimalMocks.mockPartialAnimalDto();

        //WHEN
        when(dietRepository.findByDietType(partialAnimalDto.getDietType())).thenReturn(Optional.empty());

        //THEN
        DietNotFoundException dietNotFoundException = assertThrows(DietNotFoundException.class, () -> animalService.addAnimal(partialAnimalDto));
        assertEquals(String.format(ProjectConstants.DIET_NOT_FOUND, TestConstants.DIET_NAME), dietNotFoundException.getMessage());
    }
/*
    @Test
    public void testAdoptAnimal() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setClientDto(ClientMocks.mockClientDto());
        Animal updatedAnimal =  AnimalMocks.mockAnimal();
        updatedAnimal.setClient(ClientMocks.mockClient());
        animal.setClient(null);
        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(ClientMocks.mockClient()));
        when(animalMapper.mapToAnimalDto(updatedAnimal)).thenReturn(animalDto);
        when(animalRepository.save(updatedAnimal)).thenReturn(updatedAnimal);

        //THEN
        AnimalDto result = animalService.adoptAnimal(animal.getId(), TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result, animalDto);
        assertEquals(result.getClientDto(), ClientMocks.mockClientDto());
        assertThat(result.getCageDto()).isNull();
        assertNotNull(result);
    }

    @Test
    public void testAdoptAnimalNotFoundException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setClientDto(ClientMocks.mockClientDto());

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.empty());

        //THEN
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> animalService.adoptAnimal(animal.getId(), TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.ANIMAL_NOT_FOUND, animal.getId()), animalNotFoundException.getMessage());
    }

    @Test
    public void testAdoptAnimalClientNotFoundException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setClientDto(ClientMocks.mockClientDto());

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));
        when(clientRepository.findClientByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.empty());

        //THEN
        ClientNotFoundException clientNotFoundException = assertThrows(ClientNotFoundException.class, () -> animalService.adoptAnimal(animal.getId(), TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.CLIENT_NOT_FOUND,TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME),clientNotFoundException.getMessage());
    }

    @Test
    public void testAdoptAnimalAlreadyAdoptedException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animal.setClient(ClientMocks.mockClient());
        animalDto.setClientDto(ClientMocks.mockClientDto());

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));

        //THEN
        AnimalAlreadyAdoptedException animalAlreadyAdoptedException = assertThrows(AnimalAlreadyAdoptedException.class, () -> animalService.adoptAnimal(animal.getId(), TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.ANIMAL_ADOPTED, animal.getId()), animalAlreadyAdoptedException.getMessage());
    }

    @Test
    public void testPutAnimalInCage() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setCageDto(CageMocks.mockCageDto());
        Animal updatedAnimal =  AnimalMocks.mockAnimal();
        updatedAnimal.setCage(CageMocks.mockCage());
        Cage cage = CageMocks.mockCage();

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.of(cage));
        when(animalService.showAnimalsInCage(cage.getId())).thenReturn(new ArrayList<>());
        when(animalMapper.mapToAnimalDto(updatedAnimal)).thenReturn(animalDto);
        when(animalRepository.save(updatedAnimal)).thenReturn(updatedAnimal);

        //THEN
        AnimalDto result = animalService.putAnimalInCage(animal.getId(), cage.getId());
        assertEquals(result, animalDto);
        assertEquals(result.getCageDto(), CageMocks.mockCageDto());
        assertNotNull(result);
    }

    @Test
    public void testPutAnimalInCageNotFoundException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setCageDto(CageMocks.mockCageDto());

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.empty());

        //THEN
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> animalService.putAnimalInCage(animal.getId(), 1L));
        assertEquals(String.format(ProjectConstants.ANIMAL_NOT_FOUND, animal.getId()), animalNotFoundException.getMessage());
    }

    @Test
    public void testPutAnimalInCageCageNotFoundException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setClientDto(ClientMocks.mockClientDto());
        Cage cage = CageMocks.mockCage();

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> animalService.putAnimalInCage(animal.getId(), cage.getId()));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND,cage.getId()),cageNotFoundException.getMessage());
    }

    @Test
    public void testPutAnimalInCageFullCageException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animal.setClient(ClientMocks.mockClient());
        animalDto.setClientDto(ClientMocks.mockClientDto());
        Cage cage = CageMocks.mockCage();

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));
        when(cageRepository.findById(cage.getId())).thenReturn(Optional.ofNullable(CageMocks.mockCage()));
        when(animalRepository.findAllByCage_Id(CageMocks.mockCage().getId())).thenReturn(List.of(animal, animal, animal));
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);

        //THEN
        NoPlaceInCageException noPlaceInCageException = assertThrows(NoPlaceInCageException.class, () -> animalService.putAnimalInCage(animal.getId(), cage.getId()));
        assertEquals(String.format(ProjectConstants.CAGE_FULL,cage.getId()),noPlaceInCageException.getMessage());
    }

    @Test
    public void testDeleteAdoptedAnimals() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animal.setClient(ClientMocks.mockClient());

        List<Animal> animals = new ArrayList<>();
        animals.add(animal);

        //WHEN
        when(animalRepository.findAllByClientIsNotNull()).thenReturn(animals);

        //THEN
        String result = animalService.deleteAdoptedAnimals();
        assertEquals(result, ProjectConstants.DELETED_ADOPTED_ANIMALS);
    }

    @Test
    public void testDeleteAdoptedAnimalsEmpty() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        animal.setClient(ClientMocks.mockClient());

        //WHEN
        when(animalRepository.findAllByClientIsNotNull()).thenReturn(new ArrayList<>());

        //THEN
        String result = animalService.deleteAdoptedAnimals();
        assertEquals(result, ProjectConstants.NO_ADOPTED_ANIMALS);
    }

    @Test
    public void testGetOldestAnimalInCenter() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();

        List<Animal> animals = new ArrayList<>();
        animals.add(animal);

        //WHEN
        when(animalRepository.findOldestAnimal()).thenReturn(animals);
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);

        //THEN
        AnimalDto result = animalService.getOldestAnimalInCenter();
        assertEquals(result, animalDto);
    }

    @Test
    public void testGetOldestAnimalInCenterException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalRepository.findOldestAnimal()).thenReturn(new ArrayList<>());

        //THEN
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> animalService.getOldestAnimalInCenter());
        assertEquals(ProjectConstants.ANIMAL_EMPTY, animalNotFoundException.getMessage());
    }

*/

}
