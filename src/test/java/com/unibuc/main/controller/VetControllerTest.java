package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.VetService;
import com.unibuc.main.utils.EmployeeMocks;
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
public class VetControllerTest {
    @InjectMocks
    VetController vetController;
    @Mock
    VetService vetService;
    EmployeeDto vetDto;

    @Test
    public void getAllVetsTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(vetDto);

        //WHEN
        when(vetService.getAllEmployees()).thenReturn(employeeDtos);

        //THEN
        ResponseEntity<List<EmployeeDto>> result = vetController.getAllVets();
        assertEquals(result.getBody(), employeeDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getVetByNameTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.getEmployeeByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME)).thenReturn(vetDto);

        //THEN
        ResponseEntity<EmployeeDto> result = vetController.getVetByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME);
        assertEquals(result.getBody(), vetDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewVetTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.addNewEmployee(vetDto)).thenReturn(vetDto);

        //THEN
        ResponseEntity<EmployeeDto> result = vetController.addNewVet(vetDto);
        assertEquals(result.getBody(), vetDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteEmployeeTest() {
        //GIVEN
        vetDto = null;

        //WHEN
        when( vetService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        ResponseEntity<String> result = vetController.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result.getBody(), ProjectConstants.OBJ_DELETED);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateVetTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();
        EmployeeDto updatedVet = EmployeeMocks.mockVetDto();
        updatedVet.setSalary(2200);

        //WHEN
        when(vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, vetDto)).thenReturn(updatedVet);

        //THEN
        ResponseEntity<EmployeeDto> result = vetController.updateVet(TestConstants.FIRSTNAME, TestConstants.LASTNAME, vetDto);
        assertEquals(result.getBody(), updatedVet);
        assertEquals(Objects.requireNonNull(result.getBody()).getSalary(), updatedVet.getSalary());
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateAllSalariesWithAPercentTest() {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();
        EmployeeDto updatedVet = EmployeeMocks.mockVetDto();
        updatedVet.setSalary(vetDto.getSalary() + vetDto.getSalary() * 20/100);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(updatedVet);

        //WHEN
        when(vetService.updateAllSalariesWithAPercent(20)).thenReturn(employeeDtos);

        //THEN
        ResponseEntity<List<EmployeeDto>> result = vetController.updateAllSalariesWithAPercent(20);
        assertEquals(result.getBody(), employeeDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }


}
