package com.unibuc.main.controller.IT.MockMvc;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.controller.AnimalController;
import com.unibuc.main.dto.AddAnimalDto;
import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.AdoptAnimalDto;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.service.AnimalService;
import com.unibuc.main.service.CageService;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AnimalController.class)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class AnimalControllerMockMvcTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AnimalService animalService;
    @MockBean
    CageService cageService;
    @MockBean
    DietService dietService;
    @MockBean
    ClientRepository clientRepository;
    @MockBean
    Model model;
    AnimalDto animalDto;
    AddAnimalDto addAnimalDto;
    AdoptAnimalDto adoptAnimalDto;


    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getAnimalsPageMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(animalService.findPaginatedAnimals(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(animalDtos));

        //THEN
        mockMvc.perform(get("/animals?page={page}&size={size}", currentPage, pageSize))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/animalPaginated"))
                .andExpect(model().attribute("animalPage", new PageImpl<>(animalDtos)))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(animalService, times(1)).findPaginatedAnimals(PageRequest.of(currentPage - 1, pageSize));
    }


    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getAnimalByIdMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.getAnimalById(1L)).thenReturn(animalDto);

        //THEN
        mockMvc.perform(get("/animals/{animalId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/animalDetails"))
                .andExpect(model().attribute("animal", animalDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(animalService, times(1)).getAnimalById(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void getOldestAnimalInCenterMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.getOldestAnimalInCenter()).thenReturn(animalDto);

        //THEN
        mockMvc.perform(get("/animals/getOldestAnimalInCenter"))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/animalDetails"))
                .andExpect(model().attribute("animal", animalDto))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        verify(animalService, times(1)).getOldestAnimalInCenter();
    }


    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void addAnimalFormMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        //WHEN
        when(cageService.getAllCages()).thenReturn(Collections.singletonList(CageMocks.mockCageDto()));
        when(dietService.getAllDiets()).thenReturn(Collections.singletonList(DietMocks.mockDietDto()));

        //THEN
        mockMvc.perform(request(HttpMethod.POST, "/animals/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/addAnimalForm"))
                .andExpect(model().attribute("animal", new AddAnimalDto()))
                .andExpect(model().attribute("cagesAll", Collections.singletonList(CageMocks.mockCageDto())))
                .andExpect(model().attribute("dietsAll", Collections.singletonList(DietMocks.mockDietDto())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void saveAnimalMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        //WHEN
        when(animalService.addAnimal(addAnimalDto)).thenReturn(animalDto);

        //THEN
        mockMvc.perform(post("/animals").with(csrf()).
                        flashAttr("animal", addAnimalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animals"));
        verify(animalService, times(1)).addAnimal(addAnimalDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editAnimalFormMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        adoptAnimalDto = AnimalMocks.mockAdoptAnimalDto();

        //WHEN
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(ClientMocks.mockClient()));

        //THEN
        mockMvc.perform(get("/animals/edit/{animalId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("/animalTemplates/editAnimalForm"))
                .andExpect(model().attribute("animal", new AdoptAnimalDto(1L, null)))
                .andExpect(model().attribute("clientsAll", Collections.singletonList(ClientMocks.mockClient())))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void editAnimalMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        adoptAnimalDto = AnimalMocks.mockAdoptAnimalDto();

        //WHEN
        when(animalService.adoptAnimal(1L, adoptAnimalDto.getClientId())).thenReturn(animalDto);
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(ClientMocks.mockClient()));

        //THEN
        mockMvc.perform(post("/animals/adoptAnimal/{animalId}", 1L).with(csrf()).
                        flashAttr("animal", adoptAnimalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animals"));
        verify(animalService, times(1)).adoptAnimal(1L,  adoptAnimalDto.getClientId());
    }

    @Test
    @WithMockUser(username = "admin", password = "pass123", roles = "ADMIN")
    public void deleteAdoptedAnimalsMockMvcTest() throws Exception {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.deleteAdoptedAnimals()).thenReturn(ProjectConstants.NO_ADOPTED_ANIMALS);

        //THEN
        mockMvc.perform(get("/animals/deleteAdoptedAnimals", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("result", ProjectConstants.NO_ADOPTED_ANIMALS))
                .andExpect(view().name("redirect:/animals"));
        verify(animalService, times(1)).deleteAdoptedAnimals();
    }

}
