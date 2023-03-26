package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.RegisteredVaccineService;
import com.unibuc.main.utils.MedicalRecordMocks;
import com.unibuc.main.utils.RegisteredVaccineMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisteredVaccineControllerTest {

    @InjectMocks
    RegisteredVaccineController registeredVaccineController;
    @Mock
    RegisteredVaccineService registeredVaccineService;
    RegisteredVaccineDto registeredVaccineDto;

    @Test
    public void getAllRegisteredVaccineTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        List<RegisteredVaccineDto> registeredVaccineDtos = new ArrayList<>();
        registeredVaccineDtos.add(registeredVaccineDto);

        //WHEN
        when(registeredVaccineService.getAllRegisteredVaccines()).thenReturn(registeredVaccineDtos);

        //THEN
        ResponseEntity<List<RegisteredVaccineDto>> result = registeredVaccineController.getAllRegisteredVaccines();
        assertEquals(result.getBody(), registeredVaccineDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void associateVaccinesWithMedicalRecordTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        List<VaccineDto> vaccineDtos = new ArrayList<>();
        vaccineDtos.add(registeredVaccineDto.getVaccineDto());

        //WHEN
        when(registeredVaccineService.associateVaccinesWithMedicalRecord(vaccineDtos, MedicalRecordMocks.mockMedicalRecord().getId(),
                registeredVaccineDto.getRegistrationDate())).thenReturn(Arrays.asList(registeredVaccineDto));

        //THEN
        ResponseEntity<List<RegisteredVaccineDto>> result = registeredVaccineController.associateVaccinesWithMedicalRecord(vaccineDtos, MedicalRecordMocks.mockMedicalRecord().getId(),
                registeredVaccineDto.getRegistrationDate());
        assertEquals(result.getBody(), Arrays.asList(registeredVaccineDto));
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateRegisteredVaccinePlacesTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        RegisteredVaccineDto updatedRegisteredVaccine = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        updatedRegisteredVaccine.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));

        //WHEN
        when(registeredVaccineService.updateRegistrationDate(registeredVaccineDto.getId(), new Date(2023, Calendar.NOVEMBER,10))).thenReturn(updatedRegisteredVaccine);

        //THEN
        ResponseEntity<RegisteredVaccineDto> result = registeredVaccineController.updateRegistrationDate(registeredVaccineDto.getId(), new Date(2023, Calendar.NOVEMBER,10));
        assertEquals(result.getBody(), updatedRegisteredVaccine);
        assertEquals(result.getStatusCode().value(), 200);
    }
    
    @Test
    public void deleteRegisteredVaccineTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when( registeredVaccineService.deleteVaccineFromMedicalControl(registeredVaccineDto.getId())).thenReturn(true);

        //THEN
        ResponseEntity<String> result = registeredVaccineController.deleteVaccineFromMedicalControl(registeredVaccineDto.getId());
        assertEquals(result.getBody(), ProjectConstants.OBJ_DELETED);
        assertEquals(result.getStatusCode().value(), 200);
    }
}
