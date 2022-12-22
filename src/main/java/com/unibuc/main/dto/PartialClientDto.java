package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.validation.gender.OnlyCharacterMF;
import com.unibuc.main.validation.phonenumber.PhoneNumberMatch;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PartialClientDto {

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
