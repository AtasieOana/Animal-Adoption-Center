package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.exception.EmployeeAlreadyExistsException;
import com.unibuc.main.exception.EmployeeInfoWrongException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.mapper.EmployeeMapper;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.utils.EmployeeMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaretakerServiceTest {

    @InjectMocks
    CaretakerService caretakerService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmployeeMapper employeeMapper;

    Employee caretaker;

    EmployeeDto caretakerDto;

    @Test
    public void testGetAllEmployees() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(caretaker);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(caretakerDto);

        //WHEN
        when(employeeRepository.findAllByResponsibilityNotNull()).thenReturn(employeeList);
        when(employeeMapper.mapToEmployeeDto(caretaker)).thenReturn(caretakerDto);

        //THEN
        List<EmployeeDto> result = caretakerService.getAllEmployees();
        assertEquals(result, employeeDtos);
    }

    @Test
    public void testGetEmployeeByName() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));
        when(employeeMapper.mapToEmployeeDto(caretaker)).thenReturn(caretakerDto);

        //THEN
        EmployeeDto result = caretakerService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result, caretakerDto);
        assertNotNull(result);
    }

    @Test
    public void testGetEmployeeByNameException() {
        //GIVEN
        caretaker = null;
        caretakerDto = null;

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> caretakerService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), employeeNotFoundException.getMessage());
    }

    @Test
    public void testAddNewEmployee() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.empty());
        when(employeeMapper.mapToEmployee(caretakerDto)).thenReturn(caretaker);
        when(employeeMapper.mapToEmployeeDto(caretaker)).thenReturn(caretakerDto);
        when(employeeRepository.save(caretaker)).thenReturn(caretaker);

        //THEN
        EmployeeDto result = caretakerService.addNewEmployee(caretakerDto);
        assertEquals(result, caretakerDto);
        assertThat(result.getExperience()).isNull();
        assertThat(result.getResponsibility()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewEmployeeException() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));

        //THEN
        EmployeeAlreadyExistsException employeeAlreadyExistsException = assertThrows(EmployeeAlreadyExistsException.class, () -> caretakerService.addNewEmployee(caretakerDto));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_EXISTS, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), employeeAlreadyExistsException.getMessage());
    }

    @Test
    public void testAddNewEmployeeWrongInfoException() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        caretakerDto.setExperience(2);

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.empty());

        //THEN
        EmployeeInfoWrongException employeeInfoWrongException = assertThrows(EmployeeInfoWrongException.class, () -> caretakerService.addNewEmployee(caretakerDto));
        assertEquals(ProjectConstants.CARETAKER_WRONG_INFO, employeeInfoWrongException.getMessage());
    }

    @Test
    public void testDeleteEmployee() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));

        //THEN
        Boolean result = caretakerService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME);
        assertEquals(result, true);
    }

    @Test
    public void testDeleteEmployeeException() {
        //GIVEN
        caretaker = null;
        caretakerDto = null;

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> caretakerService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), employeeNotFoundException.getMessage());
    }

    @Test
    public void testUpdateEmployee() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        Employee updatedCaretaker = EmployeeMocks.mockCaretaker();
        updatedCaretaker.setResponsibility("Check animals");
        caretakerDto.setResponsibility("Check animals");

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));
        when(employeeMapper.mapToEmployee(caretakerDto)).thenReturn(updatedCaretaker);
        when(employeeMapper.mapToEmployeeDto(updatedCaretaker)).thenReturn(caretakerDto);
        when(employeeRepository.save(updatedCaretaker)).thenReturn(updatedCaretaker);

        //THEN
        EmployeeDto result = caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto);
        assertEquals(result, caretakerDto);
        assertThat(result.getExperience()).isNull();
        assertEquals(result.getResponsibility(), "Check animals");
        assertThat(result.getResponsibility()).isNotNull();
        assertNotNull(result);
    }

    @Test
    public void testUpdateEmployeeException() {
        //GIVEN
        caretaker = null;
        caretakerDto = null;

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME,caretakerDto));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME), employeeNotFoundException.getMessage());}

    @Test
    public void testUpdateEmployeeWrongInfoException() {
        //GIVEN
        caretaker = EmployeeMocks.mockCaretaker();
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        caretakerDto.setExperience(2);

        //WHEN
        when(employeeRepository.findCaretakerByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(Optional.ofNullable(caretaker));

        //THEN
        EmployeeInfoWrongException employeeInfoWrongException = assertThrows(EmployeeInfoWrongException.class, () -> caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto));
        assertEquals(ProjectConstants.CARETAKER_WRONG_INFO, employeeInfoWrongException.getMessage());
    }

}
