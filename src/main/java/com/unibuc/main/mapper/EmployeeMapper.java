package com.unibuc.main.mapper;

import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.entity.PersonDetails;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee mapToEmployee(EmployeeDto employeeDto) {
        return Employee.builder()
                .personDetails(PersonDetails.builder().firstName(employeeDto.getFirstName())
                                .lastName(employeeDto.getLastName())
                                .phoneNumber(employeeDto.getPhoneNumber()).build())
                .employmentDate(employeeDto.getEmploymentDate())
                .salary(employeeDto.getSalary())
                .responsibility(employeeDto.getResponsibility())
                .experience(employeeDto.getExperience()).build();
    }

    public EmployeeDto mapToEmployeeDto(Employee employee){
        return EmployeeDto.builder()
                .firstName(employee.getPersonDetails().getFirstName())
                .lastName(employee.getPersonDetails().getLastName())
                .employmentDate(employee.getEmploymentDate())
                .salary(employee.getSalary())
                .phoneNumber(employee.getPersonDetails().getPhoneNumber())
                .experience(employee.getExperience())
                .responsibility(employee.getResponsibility()).build();
    }
}
