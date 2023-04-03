package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.validation.gender.OnlyCharacterMF;
import com.unibuc.main.validation.phonenumber.PhoneNumberMatch;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PartialClientDto {

    @NotBlank(message = ProjectConstants.FIRST_NAME_BLANK)
    private String firstName;

    @NotBlank(message = ProjectConstants.LAST_NAME_BLANK)
    private String lastName;

    @PhoneNumberMatch
    private String phoneNumber;
}
