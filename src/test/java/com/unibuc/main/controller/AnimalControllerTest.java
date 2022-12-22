package com.unibuc.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AnimalController.class)
public class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    AnimalService animalService;
    AnimalDto animalDto;
    PartialAnimalDto partialAnimalDto;

    @Test
    public void getAllAnimalsTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);

        //WHEN
        when(animalService.getAllAnimals()).thenReturn(animalDtos);

        //THEN
        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(animalDtos)));
    }

    @Test
    public void showAnimalsInCageTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDtoWithCage();

        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);

        //WHEN
        when(animalService.showAnimalsInCage(animalDto.getCageDto().getId())).thenReturn(animalDtos);

        //THEN
        mockMvc.perform(get("/animals/inCage/" + animalDto.getCageDto().getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(animalDtos)));
    }

    @Test
    public void getOldestAnimalInCenterTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.getOldestAnimalInCenter()).thenReturn(animalDto);

        //THEN
        mockMvc.perform(get("/animals/getOldestAnimalInCenter"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(animalDto)));
    }

    @Test
    public void addNewAnimalTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        partialAnimalDto = AnimalMocks.mockPartialAnimalDto();
        //WHEN
        when(animalService.addAnimal(partialAnimalDto)).thenReturn(animalDto);

        //THEN
        mockMvc.perform(post("/animals")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(partialAnimalDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(animalDto)));
    }

    @Test
    public void deleteAdoptedAnimalsTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        animalDto.setClientDto(ClientMocks.mockClientDto());

        //WHEN
        when( animalService.deleteAdoptedAnimals()).thenReturn(ProjectConstants.NO_ADOPTED_ANIMALS);

        //THEN
        mockMvc.perform(delete("/animals/deleteAdoptedAnimals"))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.NO_ADOPTED_ANIMALS));
    }

    @Test
    public void adoptAnimalTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        AnimalDto updatedAnimal = animalDto;
        ClientDto clientDto = ClientMocks.mockClientDto();
        updatedAnimal.setClientDto(clientDto);

        //WHEN
        when(animalService.adoptAnimal(animalDto.getId(), clientDto.getFirstName(), clientDto.getLastName())).thenReturn(updatedAnimal);

        //THEN
        mockMvc.perform(put("/animals/adoptAnimal/" + animalDto.getId() + "/" + clientDto.getFirstName() + "/" + clientDto.getLastName()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedAnimal)))
                .andExpect(jsonPath("$.clientDto.firstName").value(clientDto.getFirstName()));
    }

    @Test
    public void putAnimalInCageTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        AnimalDto updatedAnimal = animalDto;
        CageDto cageDto = CageMocks.mockCageDto();
        updatedAnimal.setCageDto(cageDto);

        //WHEN
        when(animalService.putAnimalInCage(animalDto.getId(), cageDto.getId())).thenReturn(updatedAnimal);

        //THEN
        mockMvc.perform(put("/animals/putAnimalInCage/" + animalDto.getId() + "/" + cageDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedAnimal)))
                .andExpect(jsonPath("$.cageDto.numberPlaces").value(cageDto.getNumberPlaces()));
    }

}
