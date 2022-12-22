package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.validation.gender.OnlyCharacterMF;
import com.unibuc.main.validation.phonenumber.PhoneNumberMatch;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private String gender;

    @PhoneNumberMatch
    private String phoneNumber;
}
