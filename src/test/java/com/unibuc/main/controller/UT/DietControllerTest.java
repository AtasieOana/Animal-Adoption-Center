package com.unibuc.main.controller.UT;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.DietController;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import com.unibuc.main.utils.DietMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DietControllerTest {

    @InjectMocks
    DietController dietController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    DietService dietService;
    @Mock
    RedirectAttributes redirectAttributes;
    DietDto dietDto;

    @Test
    public void getDietsPageTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();
        List<DietDto> dietDtos = new ArrayList<>();
        dietDtos.add(dietDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(dietService.findPaginatedDiets(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(dietDtos));

        //THEN
        String viewName = dietController.getDietsPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/dietTemplates/dietPaginated", viewName);
        verify(dietService, times(1)).findPaginatedDiets(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("dietPage"), argumentCaptor.capture() );

        PageImpl dietDtoArg = argumentCaptor.getValue();
        assertEquals(dietDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getDietByTypeTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.getDietByType(TestConstants.DIET_NAME)).thenReturn(dietDto);

        //THEN
        ModelAndView modelAndViewDiet = dietController.getDietByType(TestConstants.DIET_NAME);
        assertEquals("/dietTemplates/dietDetails", modelAndViewDiet.getViewName());
        verify(dietService, times(1)).getDietByType(TestConstants.DIET_NAME);
    }

    @Test
    public void addDietFormTest() {
        //GIVEN

        //WHEN

        //THEN
        String viewName = dietController.addDietForm(model);
        assertEquals("/dietTemplates/addDietForm", viewName);

        ArgumentCaptor<DietDto> argumentCaptor = ArgumentCaptor.forClass(DietDto.class);
        verify(model, times(1))
                .addAttribute(eq("diet"), argumentCaptor.capture() );

        DietDto dietDtoArg = argumentCaptor.getValue();
        assertEquals(dietDtoArg, new DietDto());
    }

    @Test
    public void saveDietTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.addDiet(dietDto)).thenReturn(dietDto);

        //THEN
        String viewName = dietController.saveDiet(dietDto, bindingResult);
        assertEquals("redirect:/diets", viewName);
        verify(dietService, times(1)).addDiet(dietDto);
    }

    @Test
    public void editDietFormTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.getDietByType(TestConstants.DIET_NAME)).thenReturn(dietDto);

        //THEN
        String viewName = dietController.editDietForm(TestConstants.DIET_NAME, model);
        assertEquals("/dietTemplates/editDietForm", viewName);
        verify(dietService, times(1)).getDietByType(TestConstants.DIET_NAME);

        ArgumentCaptor<DietDto> argumentCaptor1 = ArgumentCaptor.forClass(DietDto.class);
        verify(model, times(1))
                .addAttribute(eq("diet"), argumentCaptor1.capture() );
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(model, times(1))
                .addAttribute(eq("oldDietType"), argumentCaptor2.capture() );

        DietDto dietDtoArg = argumentCaptor1.getValue();
        assertEquals(dietDtoArg, dietDto);
        String oldDietTypeArg = argumentCaptor2.getValue();
        assertEquals(oldDietTypeArg, TestConstants.DIET_NAME);
    }

    @Test
    public void editDietTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.updateDietPartial(TestConstants.DIET_NAME, dietDto)).thenReturn(dietDto);

        //THEN
        String viewName = dietController.editDiet(TestConstants.DIET_NAME, dietDto, bindingResult);
        assertEquals("redirect:/diets", viewName);
        verify(dietService, times(1)).updateDietPartial(TestConstants.DIET_NAME, dietDto);
    }

    @Test
    public void deleteDietTest() {
        //GIVEN
        dietDto = DietMocks.mockDietDto();

        //WHEN
        when(dietService.deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME)).thenReturn(ProjectConstants.DIET_NOT_DELETED);

        //THEN
        String viewName = dietController.deleteDietIfStockEmpty(TestConstants.DIET_NAME, redirectAttributes);
        assertEquals("redirect:/diets", viewName);
        verify(dietService, times(1)).deleteDietOnlyIfStockEmpty(TestConstants.DIET_NAME);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1))
                .addAttribute(eq("result"), argumentCaptor.capture() );
        String resultArg = argumentCaptor.getValue();
        assertEquals(resultArg, ProjectConstants.DIET_NOT_DELETED);
    }
}

