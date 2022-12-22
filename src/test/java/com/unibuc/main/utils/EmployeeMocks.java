package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.entity.PersonDetails;

import java.util.Calendar;
import java.util.Date;

public class EmployeeMocks {

    public static Employee mockCaretaker() {
        return Employee.builder()
                .id(1L)
                .personDetails(PersonDetails.builder().firstName(TestConstants.FIRSTNAME)
                        .lastName(TestConstants.LASTNAME)
                        .phoneNumber(TestConstants.PHONE_NUMBER).build())
                .salary(2000)
                .employmentDate(new Date(2022,Calendar.NOVEMBER,10))
                .responsibility(TestConstants.RESPONSIBILITY)
                .build();
    }

    public static Employee mockVet() {
        return Employee.builder()
                .id(1L)
                .personDetails(PersonDetails.builder().firstName(TestConstants.FIRSTNAME)
                        .lastName(TestConstants.LASTNAME)
                        .phoneNumber(TestConstants.PHONE_NUMBER).build())
                .salary(2000)
                .employmentDate(new Date(2022,Calendar.NOVEMBER,10))
                .experience(1)
                .build();
    }

    public static EmployeeDto mockCaretakerDto() {
        return EmployeeDto.builder()
                .firstName(TestConstants.FIRSTNAME)
                .lastName(TestConstants.LASTNAME)
                .phoneNumber(TestConstants.PHONE_NUMBER)
                .salary(2000)
                .employmentDate(new Date(2022,Calendar.NOVEMBER,10))
                .responsibility(TestConstants.RESPONSIBILITY)
                .build();
    }

    public static EmployeeDto mockVetDto() {
        return EmployeeDto.builder()
                .firstName(TestConstants.FIRSTNAME)
                .lastName(TestConstants.LASTNAME)
                .phoneNumber(TestConstants.PHONE_NUMBER)
                .salary(2000)
                .employmentDate(new Date(2022,Calendar.NOVEMBER,10))
                .experience(1)
                .build();
    }

    public static Employee mockCaretaker2() {
        return Employee.builder()
                .id(1L)
                .personDetails(PersonDetails.builder().firstName(TestConstants.FIRSTNAME2)
                        .lastName(TestConstants.LASTNAME)
                        .phoneNumber(TestConstants.PHONE_NUMBER).build())
                .salary(2000)
                .employmentDate(new Date(2022,Calendar.NOVEMBER,10))
                .responsibility(TestConstants.RESPONSIBILITY)
                .build();
    }

    public static EmployeeDto mockCaretakerDto2() {
        return EmployeeDto.builder()
                .firstName(TestConstants.FIRSTNAME2)
                .lastName(TestConstants.LASTNAME)
                .phoneNumber(TestConstants.PHONE_NUMBER)
                .salary(2000)
                .employmentDate(new Date(2022,Calendar.NOVEMBER,10))
                .responsibility(TestConstants.RESPONSIBILITY)
                .build();
    }
}
