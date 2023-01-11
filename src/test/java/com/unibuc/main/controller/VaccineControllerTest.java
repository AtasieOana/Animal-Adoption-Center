package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
import com.unibuc.main.utils.VaccineMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VaccineControllerTest {
    @InjectMocks
    VaccineController vaccineController;
    @Mock
    VaccineService vaccineService;
    VaccineDto vaccineDto;
    PartialVaccineDto partialVaccineDto;

    @Test
    public void getAllVaccinesOrderByExpiredDateTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);

        //WHEN
        when(vaccineService.getAllVaccinesOrderByExpiredDate()).thenReturn(vaccineDtos);

        //THEN
        ResponseEntity<List<VaccineDto>> result = vaccineController.getAllVaccinesOrderByExpiredDate();
        assertEquals(result.getBody(), vaccineDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getAllVaccinesWithEmptyStockTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(vaccineDto);

        //WHEN
        when(vaccineService.getAllVaccinesWithEmptyStock()).thenReturn(vaccineDtos);

        //THEN
        ResponseEntity<List<VaccineDto>> result = vaccineController.getAllVaccinesWithEmptyStock();
        assertEquals(result.getBody(), vaccineDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewVaccineTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();

        //WHEN
        when(vaccineService.addVaccine(vaccineDto)).thenReturn(vaccineDto);

        //THEN
        ResponseEntity<VaccineDto> result = vaccineController.addNewVaccine(vaccineDto);
        assertEquals(result.getBody(), vaccineDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteExpiredVaccinesTest() {
        //GIVEN
        vaccineDto = null;

        //WHEN
        when( vaccineService.deleteExpiredVaccines()).thenReturn(ProjectConstants.DELETED_EXP_VACCINES);

        //THEN
        ResponseEntity<String> result = vaccineController.deleteExpiredVaccines();
        assertEquals(result.getBody(), ProjectConstants.DELETED_EXP_VACCINES);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateVaccineTest() {
        //GIVEN
        vaccineDto = VaccineMocks.mockVaccineDto();
        VaccineDto updatedVaccine = VaccineMocks.mockVaccineDto();
        updatedVaccine.setQuantityOnStock(10);
        partialVaccineDto = VaccineMocks.mockPartialVaccineDto();
        partialVaccineDto.setQuantityOnStock(10);

        //WHEN
        when(vaccineService.updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto)).thenReturn(updatedVaccine);

        //THEN
        ResponseEntity<VaccineDto> result = vaccineController.updateVaccine(TestConstants.VACCINE_NAME, partialVaccineDto);
        assertEquals(result.getBody(), updatedVaccine);
        assertEquals(Objects.requireNonNull(result.getBody()).getQuantityOnStock(), partialVaccineDto.getQuantityOnStock());
        assertEquals(result.getStatusCode().value(), 200);
    }

}
