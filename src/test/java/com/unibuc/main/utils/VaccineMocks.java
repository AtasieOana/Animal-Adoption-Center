package com.unibuc.main.utils;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.entity.PersonDetails;
import com.unibuc.main.entity.Vaccine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VaccineMocks {


    public static Vaccine mockVaccine() {
        return Vaccine.builder()
                .id(1L)
                .vaccineName(TestConstants.VACCINE_NAME)
                .expirationDate(new Date(2022,Calendar.NOVEMBER,10))
                .quantityOnStock(0)
                .build();
    }

    public static VaccineDto mockVaccineDto() {
        return VaccineDto.builder()
                .vaccineName(TestConstants.VACCINE_NAME)
                .expirationDate(new Date(2022,Calendar.NOVEMBER,10))
                .quantityOnStock(0)
                .build();
    }

    public static PartialVaccineDto mockPartialVaccineDto() {
        return PartialVaccineDto.builder()
                .expirationDate(new Date(2022,Calendar.NOVEMBER,10))
                .quantityOnStock(0)
                .build();
    }
}
