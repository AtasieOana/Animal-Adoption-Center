package com.unibuc.main.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.controller.CageController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.service.CageService;
import com.unibuc.main.utils.CageMocks;
import com.unibuc.main.utils.EmployeeMocks;
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
@WebMvcTest(controllers = CageController.class)
public class CageControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    CageService cageService;
    CageDto cageDto;
    PartialCageDto partialCageDto;

    @Test
    public void getCagesWithoutACaretakerTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        cageDto.setCaretaker(null);

        List<CageDto> cageDtos = new ArrayList<>();
        cageDtos.add(cageDto);

        //WHEN
        when(cageService.getCagesWithoutACaretaker()).thenReturn(cageDtos);

        //THEN
        mockMvc.perform(get("/cages/getCagesWithoutACaretaker"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(cageDtos)));
    }

    @Test
    public void getCageByIdTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        //WHEN
        when(cageService.getCageById(cageDto.getId())).thenReturn(cageDto);

        //THEN
        mockMvc.perform(get("/cages/" + cageDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(cageDto)));
    }

    @Test
    public void addNewCageTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        partialCageDto = CageMocks.mockPartialCageDto();

        //WHEN
        when(cageService.addCage(partialCageDto)).thenReturn(cageDto);

        //THEN
        mockMvc.perform(post("/cages")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(partialCageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberPlaces").value(cageDto.getNumberPlaces()))
                .andExpect(jsonPath("$.caretaker.firstName").value(cageDto.getCaretaker().getFirstName()));
    }

    @Test
    public void deleteCageTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();

        //WHEN
        when( cageService.deleteCage(cageDto.getId())).thenReturn(true);

        //THEN
        mockMvc.perform(delete("/cages/" + cageDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.OBJ_DELETED));
    }

    @Test
    public void updateCageCaretakerTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        CageDto updatedCage = CageMocks.mockCageDto();
        EmployeeDto caretaker = EmployeeMocks.mockCaretakerDto2();
        updatedCage.setCaretaker(caretaker);

        //WHEN
        when(cageService.updateCageCaretaker(cageDto.getId(), caretaker.getFirstName(), caretaker.getLastName())).thenReturn(updatedCage);

        //THEN
        mockMvc.perform(patch("/cages/updateCageCaretaker/" + cageDto.getId() + "/"+ caretaker.getFirstName() + "/" + caretaker.getLastName()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedCage)))
                .andExpect(jsonPath("$.caretaker.firstName").value(caretaker.getFirstName()))
                .andExpect(jsonPath("$.numberPlaces").value(3));
    }

    @Test
    public void updateCagePlacesTest() throws Exception {
        //GIVEN
        cageDto = CageMocks.mockCageDto();
        CageDto updatedCage = cageDto;
        updatedCage.setNumberPlaces(5);

        //WHEN
        when(cageService.updatePlacesInCage(cageDto.getId(), 5)).thenReturn(updatedCage);

        //THEN
        mockMvc.perform(patch("/cages/updateCagePlaces/" + cageDto.getId() + "/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedCage)))
                .andExpect(jsonPath("$.caretaker.firstName").value(EmployeeMocks.mockCaretaker().getPersonDetails().getFirstName()))
                .andExpect(jsonPath("$.numberPlaces").value(5));
    }
}
