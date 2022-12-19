package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.validation.gender.OnlyCharacterMF;
import com.unibuc.validation.phonenumber.PhoneNumberMatch;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Setter
@Getter
public class ClientDto {

    @NotNull(message = ProjectConstants.FIRST_NAME_NULL)
    private String firstName;

    @NotNull(message = ProjectConstants.LAST_NAME_NULL)
    private String lastName;

    private Date dateBecomingClient;

    private Date birthDate;

    @OnlyCharacterMF
    private Character gender;

    @PhoneNumberMatch
    private String phoneNumber;
}
