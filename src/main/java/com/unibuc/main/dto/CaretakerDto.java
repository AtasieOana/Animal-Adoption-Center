package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
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
public class CaretakerDto {

    @NotNull(message = ProjectConstants.RESPONSIBILITY_NULL)
    private String responsibility;

    @NotNull(message = ProjectConstants.FIRST_NAME_NULL)
    private String firstName;

    @NotNull(message = ProjectConstants.LAST_NAME_NULL)
    private String lastName;

    private Date employmentDate;

    @PhoneNumberMatch
    private String phoneNumber;
}
