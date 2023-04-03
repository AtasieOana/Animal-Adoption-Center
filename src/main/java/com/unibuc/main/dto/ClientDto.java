package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.validation.gender.OnlyCharacterMF;
import com.unibuc.main.validation.phonenumber.PhoneNumberMatch;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClientDto {

    @NotBlank(message = ProjectConstants.FIRST_NAME_BLANK)
    private String firstName;

    @NotNull(message = ProjectConstants.LAST_NAME_BLANK)
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBecomingClient;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @OnlyCharacterMF
    private String gender;

    @PhoneNumberMatch
    private String phoneNumber;
}
