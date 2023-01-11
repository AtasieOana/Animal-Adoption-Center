package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.CaretakerService;
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
public class CaretakerControllerTest {

    @InjectMocks
    CaretakerController caretakerController;
    @Mock
    CaretakerService caretakerService;
    EmployeeDto caretakerDto;

    @Test
    public void getAllCaretakersTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(caretakerDto);

        //WHEN
        when(caretakerService.getAllEmployees()).thenReturn(employeeDtos);

        //THEN
        ResponseEntity<List<EmployeeDto>> result = caretakerController.getAllCaretakers();
        assertEquals(result.getBody(), employeeDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getCaretakerByNameTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.getEmployeeByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME)).thenReturn(caretakerDto);

        //THEN
        ResponseEntity<EmployeeDto> result = caretakerController.getCaretakerByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME);
        assertEquals(result.getBody(), caretakerDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addNewCaretakerTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.addNewEmployee(caretakerDto)).thenReturn(caretakerDto);

        //THEN
        ResponseEntity<EmployeeDto> result = caretakerController.addNewCaretaker(caretakerDto);
        assertEquals(result.getBody(), caretakerDto);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void deleteEmployeeTest() {
        //GIVEN
        caretakerDto = null;

        //WHEN
        when( caretakerService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        ResponseEntity<String> result = caretakerController.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result.getBody(), ProjectConstants.OBJ_DELETED);
        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void updateCaretakerTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        EmployeeDto updatedCaretaker = EmployeeMocks.mockCaretakerDto();
        updatedCaretaker.setSalary(2200);

        //WHEN
        when(caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto)).thenReturn(updatedCaretaker);

        //THEN
        ResponseEntity<EmployeeDto> result = caretakerController.updateCaretaker(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto);
        assertEquals(result.getBody(), updatedCaretaker);
        assertEquals(result.getStatusCode().value(), 200);
        assertEquals(Objects.requireNonNull(result.getBody()).getSalary(), updatedCaretaker.getSalary());
    }

    @Test
    public void updateAllSalariesWithAPercentTest() {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        EmployeeDto updatedCaretaker = EmployeeMocks.mockCaretakerDto();
        updatedCaretaker.setSalary(caretakerDto.getSalary() + caretakerDto.getSalary() * 20/100);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(updatedCaretaker);

        //WHEN
        when(caretakerService.updateAllSalariesWithAPercent(20)).thenReturn(employeeDtos);

        //THEN
        ResponseEntity<List<EmployeeDto>> result = caretakerController.updateAllSalariesWithAPercent(20);
        assertEquals(result.getBody(), employeeDtos);
        assertEquals(result.getStatusCode().value(), 200);
    }


}
