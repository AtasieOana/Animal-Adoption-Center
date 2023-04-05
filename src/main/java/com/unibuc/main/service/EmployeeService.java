package com.unibuc.main.service;

import com.unibuc.main.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeByName(String firstName, String lastName);

    EmployeeDto addNewEmployee(EmployeeDto employeeDto);

    boolean deleteEmployee(String firstName, String lastName);

    EmployeeDto updateEmployee(String oldFirstName, String oldLastName, EmployeeDto newEmployeeDto);
}
