package com.unibuc.main.utils;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.entity.PersonDetails;
import com.unibuc.main.entity.Vaccine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VaccineMocks {

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Vaccine mockVaccine() {
        String date_string = "26-09-2021";
        try {
            Date date = formatter.parse(date_string);
            return Vaccine.builder()
                    .id(1L)
                    .vaccineName(TestConstants.VACCINE_NAME)
                    .expirationDate(date)
                    .quantityOnStock(0)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static VaccineDto mockVaccineDto() {
        String date_string = "26-09-2021";
        try {
            Date date = formatter.parse(date_string);
            return VaccineDto.builder()
                    .vaccineName(TestConstants.VACCINE_NAME)
                    .expirationDate(date)
                    .quantityOnStock(0)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static PartialVaccineDto mockPartialVaccineDto() {
        String date_string = "26-09-2021";
        try {
            Date date = formatter.parse(date_string);
            return PartialVaccineDto.builder()
                    .expirationDate(date)
                    .quantityOnStock(0)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vaccine mockVaccine2() {
        String date_string = "26-09-2024";
        try {
            Date date = formatter.parse(date_string);
            return Vaccine.builder()
                    .id(2L)
                    .vaccineName(TestConstants.VACCINE_NAME2)
                    .expirationDate(date)
                    .quantityOnStock(0)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static VaccineDto mockVaccineDto2() {
        String date_string = "26-09-2021";
        try {
            Date date = formatter.parse(date_string);
            return VaccineDto.builder()
                    .vaccineName(TestConstants.VACCINE_NAME2)
                    .expirationDate(date)
                    .quantityOnStock(0)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
