package com.unibuc.main.service.employees;

import com.unibuc.main.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    public List<EmployeeDto> getAllEmployees();

    public EmployeeDto getEmployeeByName(String firstName, String lastName);

    public EmployeeDto addNewEmployee(EmployeeDto employeeDto);

    public boolean deleteEmployee(String firstName, String lastName);

    public EmployeeDto updateEmployee(String oldFirstName, String oldLastName, EmployeeDto newEmployeeDto);

    public List<EmployeeDto> updateAllSalariesWithAPercent(Integer percent);

}
