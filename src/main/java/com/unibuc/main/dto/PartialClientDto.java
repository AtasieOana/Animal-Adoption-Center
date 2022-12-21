package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.validation.gender.OnlyCharacterMF;
import com.unibuc.main.validation.phonenumber.PhoneNumberMatch;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@Setter
@Getter
public class PartialClientDto {

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
