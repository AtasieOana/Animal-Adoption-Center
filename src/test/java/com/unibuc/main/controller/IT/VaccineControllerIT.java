package com.unibuc.main.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.VaccineController;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
import com.unibuc.main.utils.VaccineMocks;
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
@WebMvcTest(controllers = VaccineController.class)
public class VaccineControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    VaccineService vaccineService;
    VaccineDto vaccineDto;
    PartialVaccineDto partialVaccineDto;

    @Test
    public void getAllVaccinesOrderByExpiredDateTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);

        //WHEN
        when(vaccineService.getAllVaccinesOrderByExpiredDate()).thenReturn(vaccineDtos);

        //THEN
        mockMvc.perform(get("/vaccines/getAllVaccinesOrderByExpiredDate"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(vaccineDtos)));
    }

    @Test
    public void getAllVaccinesWithEmptyStockTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);

        //WHEN
        when(vaccineService.getAllVaccinesWithEmptyStock()).thenReturn(vaccineDtos);

        //THEN
        mockMvc.perform(get("/vaccines/getAllVaccinesWithEmptyStock"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(vaccineDtos)));
    }

    @Test
    public void addNewVaccineTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.addVaccine(vaccineDto)).thenReturn(vaccineDto);

        //THEN
        mockMvc.perform(post("/vaccines")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vaccineDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vaccineName").value(vaccineDto.getVaccineName()))
                .andExpect(jsonPath("$.quantityOnStock").value(0));
    }

    @Test
    public void deleteExpiredVaccinesTest() throws Exception {
        //GIVEN
        vaccineDto = null;

        //WHEN
        when( vaccineService.deleteExpiredVaccines()).thenReturn(ProjectConstants.DELETED_EXP_VACCINES);

        //THEN
        mockMvc.perform(delete("/vaccines/deleteExpiredVaccines"))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.DELETED_EXP_VACCINES));
    }

    @Test
    public void updateVaccineTest() throws Exception {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();
        VaccineDto updatedVaccine = vaccineDto;
        updatedVaccine.setQuantityOnStock(10);
        partialVaccineDto = VaccineMocks.mockPartialVaccineDto();
        partialVaccineDto.setQuantityOnStock(10);

        //WHEN
        when(vaccineService.updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto)).thenReturn(updatedVaccine);

        //THEN
        mockMvc.perform(put("/vaccines/" + TestConstants.VACCINE_NAME)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(partialVaccineDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedVaccine)))
                .andExpect(jsonPath("$.vaccineName").value(vaccineDto.getVaccineName()))
                .andExpect(jsonPath("$.quantityOnStock").value(10));
    }

}
