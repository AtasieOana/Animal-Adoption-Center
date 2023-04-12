package com.unibuc.main.controller.UT;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.controller.AnimalController;
import com.unibuc.main.dto.*;
import com.unibuc.main.entity.Client;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.service.AnimalService;
import com.unibuc.main.service.CageService;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.AnimalMocks;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.ClientMocks;
import com.unibuc.main.utils.DietMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalControllerTest {

    @InjectMocks
    AnimalController animalController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    RedirectAttributes redirectAttributes;
    @Mock
    AnimalService animalService;
    @Mock
    CageService cageService;
    @Mock
    DietService dietService;
    @Mock
    ClientRepository clientRepository;
    AnimalDto animalDto;
    AddAnimalDto addAnimalDto;
    AdoptAnimalDto adoptAnimalDto;
    
    @Test
    public void getAnimalsPageTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        List<AnimalDto> animalDtos = new ArrayList<>();
        animalDtos.add(animalDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(animalService.findPaginatedAnimals(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(animalDtos));

        //THEN
        String viewName = animalController.getAnimalsPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/animalTemplates/animalPaginated", viewName);
        verify(animalService, times(1)).findPaginatedAnimals(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("animalPage"), argumentCaptor.capture() );

        PageImpl animalDtoArg = argumentCaptor.getValue();
        assertEquals(animalDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getAnimalByIdTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.getAnimalById(1L)).thenReturn(animalDto);

        //THEN
        ModelAndView modelAndViewAnimal = animalController.getAnimalById(1L);
        assertEquals("/animalTemplates/animalDetails", modelAndViewAnimal.getViewName());
        verify(animalService, times(1)).getAnimalById(1L);
    }

    @Test
    public void getOldestAnimalInCenterTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.getOldestAnimalInCenter()).thenReturn(animalDto);

        //THEN
        ModelAndView modelAndViewAnimal = animalController.getOldestAnimalInCenter();
        assertEquals("/animalTemplates/animalDetails", modelAndViewAnimal.getViewName());
        verify(animalService, times(1)).getOldestAnimalInCenter();
    }

    @Test
    public void addAnimalFormTest() {
        //GIVEN

        //WHEN
        when(cageService.getAllCages()).thenReturn(Collections.singletonList(CageMocks.mockCageDto()));
        when(dietService.getAllDiets()).thenReturn(Collections.singletonList(DietMocks.mockDietDto()));

        //THEN
        String viewName = animalController.addAnimalForm(model);
        assertEquals("/animalTemplates/addAnimalForm", viewName);
        verify(cageService, times(1)).getAllCages();
        verify(dietService, times(1)).getAllDiets();

        ArgumentCaptor<AddAnimalDto> argumentCaptor = ArgumentCaptor.forClass(AddAnimalDto.class);
        verify(model, times(1))
                .addAttribute(eq("animal"), argumentCaptor.capture() );
        ArgumentCaptor<List<CageDto>> argumentCaptorCage = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("cagesAll"), argumentCaptorCage.capture() );
        ArgumentCaptor<List<DietDto>> argumentCaptorDiet = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("dietsAll"), argumentCaptorDiet.capture() );

        AddAnimalDto animalDtoArg = argumentCaptor.getValue();
        assertEquals(animalDtoArg, new AddAnimalDto());
        List<CageDto> cagesAllArg = argumentCaptorCage.getValue();
        assertEquals(cagesAllArg, Collections.singletonList(CageMocks.mockCageDto()));
        List<DietDto> dietsAllArg = argumentCaptorDiet.getValue();
        assertEquals(dietsAllArg, Collections.singletonList(DietMocks.mockDietDto()));
    }

    @Test
    public void saveAnimalTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        addAnimalDto = AnimalMocks.mockAddAnimalDto();

        //WHEN
        when(animalService.addAnimal(addAnimalDto)).thenReturn(animalDto);

        //THEN
        String viewName = animalController.saveAnimal(addAnimalDto, bindingResult, model);
        assertEquals("redirect:/animals", viewName);
        verify(animalService, times(1)).addAnimal(addAnimalDto);
    }

    @Test
    public void editAnimalFormTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        adoptAnimalDto = new AdoptAnimalDto(1L, null);

        //WHEN
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(ClientMocks.mockClient()));

        //THEN
        String viewName = animalController.editAnimalForm(1L, model);
        assertEquals("/animalTemplates/editAnimalForm", viewName);
        verify(clientRepository, times(1)).findAll();

        ArgumentCaptor<AdoptAnimalDto> argumentCaptor = ArgumentCaptor.forClass(AdoptAnimalDto.class);
        verify(model, times(1))
                .addAttribute(eq("animal"), argumentCaptor.capture() );
        ArgumentCaptor<List<Client>> argumentCaptorClient = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("clientsAll"), argumentCaptorClient.capture() );

        AdoptAnimalDto animalDtoArg = argumentCaptor.getValue();
        assertEquals(animalDtoArg, adoptAnimalDto);
        List<Client> clientsAllArg = argumentCaptorClient.getValue();
        assertEquals(clientsAllArg, Collections.singletonList(ClientMocks.mockClient()));
    }

    @Test
    public void editAnimalTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();
        adoptAnimalDto = AnimalMocks.mockAdoptAnimalDto();

        //WHEN
        when(animalService.adoptAnimal(1L, adoptAnimalDto.getClientId())).thenReturn(animalDto);

        //THEN
        String viewName = animalController.adoptAnimal(1L, adoptAnimalDto, bindingResult, model);
        assertEquals("redirect:/animals", viewName);
        verify(animalService, times(1)).adoptAnimal(1L, adoptAnimalDto.getClientId());
    }

    @Test
    public void deleteAnimalTest() {
        //GIVEN
        animalDto = AnimalMocks.mockAnimalDto();

        //WHEN
        when(animalService.deleteAdoptedAnimals()).thenReturn(ProjectConstants.DELETED_ADOPTED_ANIMALS);

        //THEN
        String viewName = animalController.deleteAdoptedAnimals(redirectAttributes);
        assertEquals("redirect:/animals", viewName);
        verify(animalService, times(1)).deleteAdoptedAnimals();

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1))
                .addAttribute(eq("result"), argumentCaptor.capture() );
        String resultArg = argumentCaptor.getValue();
        assertEquals(resultArg, ProjectConstants.DELETED_ADOPTED_ANIMALS);
    }
}

