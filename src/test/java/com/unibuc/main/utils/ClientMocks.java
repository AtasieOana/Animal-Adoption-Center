package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Client;
import com.unibuc.main.entity.PersonDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClientMocks {

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Client mockClient() {
        String date_string = "02-01-2001";
        try {
            Date date = formatter.parse(date_string);
            return Client.builder()
                    .id(1L)
                    .personDetails(PersonDetails.builder().firstName(TestConstants.FIRSTNAME)
                            .lastName(TestConstants.LASTNAME)
                            .phoneNumber(TestConstants.PHONE_NUMBER).build())
                    .birthDate(date)
                    .dateBecomingClient(new Date(2022,Calendar.NOVEMBER,10))
                    .gender("M")
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClientDto mockClientDto() {
        String date_string = "02-01-2001";
        try {
            Date date = formatter.parse(date_string);
            return ClientDto.builder()
                    .firstName(TestConstants.FIRSTNAME)
                    .lastName(TestConstants.LASTNAME)
                    .phoneNumber(TestConstants.PHONE_NUMBER)
                    .birthDate(date)
                    .dateBecomingClient(new Date(2022,Calendar.NOVEMBER,10))
                    .gender("M")
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static PartialClientDto mockPartialClientDto() {
        return PartialClientDto.builder()
                .firstName(TestConstants.FIRSTNAME)
                .lastName(TestConstants.LASTNAME)
                .phoneNumber(TestConstants.PHONE_NUMBER)
                .build();
    }


    public static ClientDto mockClientDto2() {
        String date_string = "02-01-2001";
        try {
            Date date = formatter.parse(date_string);
            return ClientDto.builder()
                    .firstName(TestConstants.FIRSTNAME2)
                    .lastName(TestConstants.LASTNAME)
                    .phoneNumber(TestConstants.PHONE_NUMBER)
                    .birthDate(date)
                    .dateBecomingClient(new Date(2022,Calendar.NOVEMBER,10))
                    .gender("M")
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
