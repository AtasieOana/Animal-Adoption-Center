package com.unibuc.main.controller;

import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.DietMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DietControllerTest {

    @InjectMocks
    DietController dietController;
    @Mock
    DietService dietService;
    @Mock
    Model model;
    @Captor
    ArgumentCaptor<DietDto> argumentCaptor;
    DietDto dietDto;

    @Test
    public void addDietFormTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN

        //THEN
        String viewName = dietController.addDietForm(model);
        assertEquals("/dietTemplates/addDietForm", viewName);

        verify(model, times(1))
                .addAttribute(eq("diet"), argumentCaptor.capture() );

        DietDto productArg = argumentCaptor.getValue();
        assertEquals(productArg, new DietDto());
    }

    @Test
    public void saveDietTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.addDiet(dietDto)).thenReturn(dietDto);

        //THEN
        String viewName = dietController.addDietForm(model);
        assertEquals("/diets", viewName);
    }
}

