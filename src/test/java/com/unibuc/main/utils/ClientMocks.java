package com.unibuc.main.utils;

import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.entity.Client;
import com.unibuc.main.entity.PersonDetails;

import java.util.Calendar;
import java.util.Date;

public class ClientMocks {

    public static Client mockClient() {
        return Client.builder()
                .id(1L)
                .personDetails(PersonDetails.builder().firstName(TestConstants.FIRSTNAME)
                        .lastName(TestConstants.LASTNAME)
                        .phoneNumber(TestConstants.PHONE_NUMBER).build())
                .birthDate(new Date(2001,Calendar.DECEMBER,11))
                .dateBecomingClient(new Date(2022,Calendar.NOVEMBER,10))
                .gender("M")
                .build();
    }

    public static ClientDto mockClientDto() {
        return ClientDto.builder()
                .firstName(TestConstants.FIRSTNAME)
                .lastName(TestConstants.LASTNAME)
                .phoneNumber(TestConstants.PHONE_NUMBER)
                .birthDate(new Date(2001,Calendar.DECEMBER,11))
                .dateBecomingClient(new Date(2022,Calendar.NOVEMBER,10))
                .gender("M")
                .build();
    }

    public static PartialClientDto mockPartialClientDto() {
        return PartialClientDto.builder()
                .firstName(TestConstants.FIRSTNAME)
                .lastName(TestConstants.LASTNAME)
                .phoneNumber(TestConstants.PHONE_NUMBER)
                .build();
    }
}
