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
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VetServiceTest {

    @InjectMocks
    VetService vetService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmployeeMapper employeeMapper;

    Employee vet;

    EmployeeDto vetDto;

    @Test
    public void testGetAllEmployees() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(vet);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(vetDto);

        //WHEN
        when(employeeRepository.findAllByExperienceNotNull()).thenReturn(employeeList);
        when(employeeMapper.mapToEmployeeDto(vet)).thenReturn(vetDto);

        //THEN
        List<EmployeeDto> result = vetService.getAllEmployees();
        assertEquals(result, employeeDtos);
    }

    @Test
    public void testGetEmployeeByName() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));
        when(employeeMapper.mapToEmployeeDto(vet)).thenReturn(vetDto);

        //THEN
        EmployeeDto result = vetService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
        assertEquals(result, vetDto);
        assertNotNull(result);
    }

    @Test
    public void testGetEmployeeByNameException() {
        //GIVEN
        vet = null;
        vetDto = null;

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> vetService.getEmployeeByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME_VET), employeeNotFoundException.getMessage());
    }

    @Test
    public void testAddNewEmployee() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.empty());
        when(employeeMapper.mapToEmployee(vetDto)).thenReturn(vet);
        when(employeeMapper.mapToEmployeeDto(vet)).thenReturn(vetDto);
        when(employeeRepository.save(vet)).thenReturn(vet);

        //THEN
        EmployeeDto result = vetService.addNewEmployee(vetDto);
        assertEquals(result, vetDto);
        assertThat(result.getExperience()).isNotNull();
        assertThat(result.getResponsibility()).isNull();
        assertNotNull(result);
    }

    @Test
    public void testAddNewEmployeeException() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));

        //THEN
        EmployeeAlreadyExistsException employeeAlreadyExistsException = assertThrows(EmployeeAlreadyExistsException.class, () -> vetService.addNewEmployee(vetDto));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_EXISTS, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME_VET), employeeAlreadyExistsException.getMessage());
    }

    @Test
    public void testAddNewEmployeeWrongInfoException() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();
        vetDto.setResponsibility(TestConstants.RESPONSIBILITY);

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.empty());

        //THEN
        EmployeeInfoWrongException employeeInfoWrongException = assertThrows(EmployeeInfoWrongException.class, () -> vetService.addNewEmployee(vetDto));
        assertEquals(ProjectConstants.VET_WRONG_INFO, employeeInfoWrongException.getMessage());
    }

    @Test
    public void testDeleteEmployee() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));

        //THEN
        Boolean result = vetService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET);
        assertEquals(result, true);
    }

    @Test
    public void testDeleteEmployeeException() {
        //GIVEN
        vet = null;
        vetDto = null;

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> vetService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME_VET), employeeNotFoundException.getMessage());
    }

    @Test
    public void testUpdateEmployee() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();

        Employee updatedVet = EmployeeMocks.mockVet();
        updatedVet.setExperience(2);
        vetDto.setExperience(2);

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));
        when(employeeMapper.mapToEmployee(vetDto)).thenReturn(updatedVet);
        when(employeeMapper.mapToEmployeeDto(updatedVet)).thenReturn(vetDto);
        when(employeeRepository.save(updatedVet)).thenReturn(updatedVet);

        //THEN
        EmployeeDto result = vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto);
        assertEquals(result, vetDto);
        assertThat(result.getExperience()).isNotNull();
        assertEquals(result.getExperience(), 2);
        assertThat(result.getResponsibility()).isNull();
        assertNotNull(result);
    }

    @Test
    public void testUpdateEmployeeException() {
        //GIVEN
        vet = null;
        vetDto = null;

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));

        //THEN
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class, () -> vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET,vetDto));
        assertEquals(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, TestConstants.FIRSTNAME + " " + TestConstants.LASTNAME_VET), employeeNotFoundException.getMessage());
    }

    @Test
    public void testUpdateEmployeeWrongInfoException() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();
        vetDto.setResponsibility(TestConstants.RESPONSIBILITY);

        //WHEN
        when(employeeRepository.findVetByName(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET)).thenReturn(Optional.ofNullable(vet));

        //THEN
        EmployeeInfoWrongException employeeInfoWrongException = assertThrows(EmployeeInfoWrongException.class, () -> vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME_VET, vetDto));
        assertEquals(ProjectConstants.VET_WRONG_INFO, employeeInfoWrongException.getMessage());
    }

    @Test
    public void testFindPaginatedCaretakers() {
        //GIVEN
        vet = EmployeeMocks.mockVet();
        vetDto = EmployeeMocks.mockVetDto();
        Pageable pageable = PageRequest.of(0,20);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(vet);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(vetDto);

        PagedListHolder<EmployeeDto> pagedListHolder = new PagedListHolder<>(employeeDtos);
        pagedListHolder.setPageSize(0);
        pagedListHolder.setPage(20);

        //WHEN
        when(employeeRepository.findAllByExperienceNotNull()).thenReturn(employeeList);
        when(employeeMapper.mapToEmployeeDto(vet)).thenReturn(vetDto);

        //THEN
        Page<EmployeeDto> result = vetService.findPaginatedEmployees(pageable, 0, 20);
        assertEquals(result, new PageImpl<>(pagedListHolder.getPageList(), pageable, employeeDtos.size()));
    }
}
