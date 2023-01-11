package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.service.AnimalService;
import com.unibuc.main.utils.AnimalMocks;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.ClientMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalControllerTest {

    @InjectMocks
    AnimalController animalController;
    @Mock
    AnimalService animalService;
    AnimalDto animalDto;
    PartialAnimalDto partialAnimalDto;

    @Test
    public void getAllAnimalsTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);

        //WHEN
        when(animalService.getAllAnimals()).thenReturn(animalDtos);

        //THEN
        ResponseEntity<List<AnimalDto>> result = animalController.getAllAnimals();
        assertEquals(result.getBody(), animalDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void showAnimalsInCageTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDtoWithCage();

        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);

        //WHEN
        when(animalService.showAnimalsInCage(animalDto.getCageDto().getId())).thenReturn(animalDtos);

        //THEN
        ResponseEntity<List<AnimalDto>> result = animalController.showAnimalsInCage(animalDto.getCageDto().getId());
        assertEquals(result.getBody(), animalDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getOldestAnimalInCenterTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.getOldestAnimalInCenter()).thenReturn(animalDto);

        //THEN
        ResponseEntity<AnimalDto> result = animalController.getOldestAnimalInCenter();
        assertEquals(result.getBody(), animalDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewAnimalTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        partialAnimalDto = AnimalMocks.mockPartialAnimalDto();
        //WHEN
        when(animalService.addAnimal(partialAnimalDto)).thenReturn(animalDto);

        //THEN
        ResponseEntity<AnimalDto> result = animalController.addNewAnimal(partialAnimalDto);
        assertEquals(result.getBody(), animalDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteAdoptedAnimalsTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setClientDto(ClientMocks.mockClientDto());

        //WHEN
        when( animalService.deleteAdoptedAnimals()).thenReturn(ProjectConstants.DELETED_ADOPTED_ANIMALS);

        //THEN
        ResponseEntity<String> result = animalController.deleteAdoptedAnimals();
        assertEquals(result.getBody(), ProjectConstants.DELETED_ADOPTED_ANIMALS);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void adoptAnimalTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        AnimalDto updatedAnimal = AnimalMocks.mockAnimalDto();
        ClientDto clientDto = ClientMocks.mockClientDto();
        updatedAnimal.setClientDto(clientDto);

        //WHEN
        when(animalService.adoptAnimal(animalDto.getId(), clientDto.getFirstName(), clientDto.getLastName())).thenReturn(updatedAnimal);

        //THEN
        ResponseEntity<AnimalDto> result = animalController.adoptAnimal(animalDto.getId(), clientDto.getFirstName(), clientDto.getLastName());
        assertEquals(result.getBody(), updatedAnimal);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void putAnimalInCageTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        AnimalDto updatedAnimal = AnimalMocks.mockAnimalDto();
        CageDto cageDto = CageMocks.mockCageDto();
        updatedAnimal.setCageDto(cageDto);

        //WHEN
        when(animalService.putAnimalInCage(animalDto.getId(), cageDto.getId())).thenReturn(updatedAnimal);

        //THEN
        ResponseEntity<AnimalDto> result = animalController.putAnimalInCage(animalDto.getId(), cageDto.getId());
        assertEquals(result.getBody(), updatedAnimal);
        assertEquals(result.getStatusCode().value(), 200);
    }

}
