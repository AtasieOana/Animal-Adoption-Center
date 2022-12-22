package com.unibuc.main.service.employees;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.exception.EmployeeAlreadyExistsException;
import com.unibuc.main.exception.EmployeeInfoWrongException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.mapper.EmployeeMapper;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.employees.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VetService implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAllByExperienceNotNull()
                .stream().map(e -> employeeMapper.mapToEmployeeDto(e))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeByName(String firstName, String lastName) {
        Optional<Employee> employee = employeeRepository.findVetByName(firstName, lastName);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, firstName + ' ' + lastName));
        }
        return employeeMapper.mapToEmployeeDto(employee.get());
    }

    @Override
    public EmployeeDto addNewEmployee(EmployeeDto employeeDto) {
        if(employeeRepository.findVetByName(employeeDto.getFirstName(), employeeDto.getLastName()).isPresent()){
            throw new EmployeeAlreadyExistsException(String.format(ProjectConstants.EMPLOYEE_EXISTS,employeeDto.getFirstName() + " " + employeeDto.getLastName()));
        }
        if(employeeDto.getResponsibility() != null || employeeDto.getExperience() == null){
            throw new EmployeeInfoWrongException(ProjectConstants.VET_WRONG_INFO);
        }
        return employeeMapper.mapToEmployeeDto(employeeRepository.save(employeeMapper.mapToEmployee(employeeDto)));
    }

    @Override
    public boolean deleteEmployee(String firstName, String lastName) {
        Optional<Employee> employee = employeeRepository.findVetByName(firstName, lastName);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, firstName + ' ' + lastName));
        }
        employeeRepository.delete(employee.get());
        return true;
    }

    @Override
    public EmployeeDto updateEmployee(String oldFirstName, String oldLastName, EmployeeDto newEmployeeDto) {
        Optional<Employee> employee = employeeRepository.findVetByName(oldFirstName, oldLastName);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, oldFirstName + ' ' + oldLastName));
        }
        if(newEmployeeDto.getResponsibility() != null || newEmployeeDto.getExperience() == null){
            throw new EmployeeInfoWrongException(String.format(ProjectConstants.VET_WRONG_INFO));
        }
        Employee newEmployee = employeeMapper.mapToEmployee(newEmployeeDto);
        newEmployee.setId(employee.get().getId());
        return employeeMapper.mapToEmployeeDto(employeeRepository.save(newEmployee));
    }

    @Override
    public List<EmployeeDto> updateAllSalariesWithAPercent(Integer percent) {
        return employeeRepository.findAllByExperienceNotNull()
                .stream().map(e -> {employeeRepository.delete(e); e.setSalary(e.getSalary() + (percent * e.getSalary())/100); return employeeMapper.mapToEmployeeDto(employeeRepository.save(e));})
                            .collect(Collectors.toList());
    }

}
