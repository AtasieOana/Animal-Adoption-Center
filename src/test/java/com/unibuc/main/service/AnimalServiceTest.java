package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.AddAnimalDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.exception.*;
import com.unibuc.main.mapper.AnimalMapper;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.CageRepository;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.repository.DietRepository;
import com.unibuc.main.utils.AnimalMocks;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Mock
    MedicalRecordService medicalRecordService;

    Animal animal;

    AnimalDto animalDto;
    
    AddAnimalDto addAnimalDto;

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
    public void testGetAnimalById() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.ofNullable(animal));
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);

        //THEN
        AnimalDto result = animalService.getAnimalById(animal.getId());
        assertEquals(result, animalDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void testGetAnimalByIdNotFoundException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.empty());

        //THEN
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> animalService.getAnimalById(animal.getId()));
        assertEquals(String.format(ProjectConstants.ANIMAL_NOT_FOUND, animal.getId()), animalNotFoundException.getMessage());
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

    @Test
    public void testAddAnimal() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        //WHEN
        when(dietRepository.findByDietType(addAnimalDto.getDietType())).thenReturn(Optional.ofNullable(animal.getDiet()));
        when(animalMapper.mapPartialToAnimal(addAnimalDto)).thenReturn(animal);
        when(cageRepository.findById(addAnimalDto.getCageId())).thenReturn(Optional.ofNullable(animal.getCage()));
        when(animalRepository.findAllByCageId(addAnimalDto.getCageId())).thenReturn(Collections.singletonList(animal));
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);
        when(animalRepository.save(animal)).thenReturn(animal);

        //THEN
        AnimalDto result = animalService.addAnimal(addAnimalDto);
        assertEquals(result, animalDto);
        assertEquals(result.getDietDto(), animalDto.getDietDto());
        assertThat(result.getClientDto()).isNull();
        assertEquals(result.getCageDto(), animalDto.getCageDto());
        assertNotNull(result);
    }

    @Test
    public void testAddAnimalDietException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        //WHEN
        when(dietRepository.findByDietType(addAnimalDto.getDietType())).thenReturn(Optional.empty());

        //THEN
        DietNotFoundException dietNotFoundException = assertThrows(DietNotFoundException.class, () -> animalService.addAnimal(addAnimalDto));
        assertEquals(String.format(ProjectConstants.DIET_NOT_FOUND, TestConstants.DIET_NAME), dietNotFoundException.getMessage());
    }

    @Test
    public void testAddAnimalCageException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        //WHEN
        when(dietRepository.findByDietType(addAnimalDto.getDietType())).thenReturn(Optional.ofNullable(animal.getDiet()));
        when(animalMapper.mapPartialToAnimal(addAnimalDto)).thenReturn(animal);
        when(cageRepository.findById(addAnimalDto.getCageId())).thenReturn(Optional.empty());

        //THEN
        CageNotFoundException cageNotFoundException = assertThrows(CageNotFoundException.class, () -> animalService.addAnimal(addAnimalDto));
        assertEquals(String.format(ProjectConstants.CAGE_NOT_FOUND, 1L), cageNotFoundException.getMessage());
    }

    @Test
    public void testAddAnimalNoPlaceCageException() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        animals.add(animal);
        animals.add(animal);

        //WHEN
        when(dietRepository.findByDietType(addAnimalDto.getDietType())).thenReturn(Optional.ofNullable(animal.getDiet()));
        when(animalMapper.mapPartialToAnimal(addAnimalDto)).thenReturn(animal);
        when(cageRepository.findById(addAnimalDto.getCageId())).thenReturn(Optional.ofNullable(animal.getCage()));
        when(animalRepository.findAllByCageId(addAnimalDto.getCageId())).thenReturn(animals);

        //THEN
        NoPlaceInCageException noPlaceInCageException = assertThrows(NoPlaceInCageException.class, () -> animalService.addAnimal(addAnimalDto));
        assertEquals(String.format(ProjectConstants.CAGE_FULL, 1L), noPlaceInCageException.getMessage());
    }

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
        when(clientRepository.findById(ClientMocks.mockClient().getId())).thenReturn(Optional.ofNullable(ClientMocks.mockClient()));
        when(animalMapper.mapToAnimalDto(updatedAnimal)).thenReturn(animalDto);
        when(animalRepository.save(animal)).thenReturn(updatedAnimal);

        //THEN
        AnimalDto result = animalService.adoptAnimal(animal.getId(), ClientMocks.mockClient().getId());
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
        AnimalNotFoundException animalNotFoundException = assertThrows(AnimalNotFoundException.class, () -> animalService.adoptAnimal(animal.getId(), ClientMocks.mockClient().getId()));
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
        when(clientRepository.findById(ClientMocks.mockClient().getId())).thenReturn(Optional.empty());

        //THEN
        ClientNotFoundException clientNotFoundException = assertThrows(ClientNotFoundException.class, () -> animalService.adoptAnimal(animal.getId(), ClientMocks.mockClient().getId()));
        assertEquals(String.format(ProjectConstants.CLIENT_NOT_ID, ClientMocks.mockClient().getId()),clientNotFoundException.getMessage());
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
        AnimalAlreadyAdoptedException animalAlreadyAdoptedException = assertThrows(AnimalAlreadyAdoptedException.class, () -> animalService.adoptAnimal(animal.getId(), ClientMocks.mockClient().getId()));
        assertEquals(String.format(ProjectConstants.ANIMAL_ADOPTED, animal.getId()), animalAlreadyAdoptedException.getMessage());
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
        when(medicalRecordService.deleteMedicalRecordAnimals(animals)).thenReturn(true);

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
    public void testFindPaginatedDiets() {
        //GIVEN
        animal = AnimalMocks.mockAnimal();
        animalDto = AnimalMocks.mockAnimalDto();
        Pageable pageable = PageRequest.of(0,20);

        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);

        //WHEN
        when(animalRepository.findAll(pageable)).thenReturn(new PageImpl<>(animals));
        when(animalMapper.mapToAnimalDto(animal)).thenReturn(animalDto);

        //THEN
        Page<AnimalDto> result = animalService.findPaginatedAnimals(pageable);
        assertEquals(result, new PageImpl<>(animalDtos));
    }
}
