package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
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
public class EmployeeDto {

    @NotNull(message = ProjectConstants.FIRST_NAME_NULL)
    private String firstName;

    @NotNull(message = ProjectConstants.LAST_NAME_NULL)
    private String lastName;

    private Date employmentDate;

    private Integer salary;

    @PhoneNumberMatch
    private String phoneNumber;

    private String responsibility;

    private Integer experience;
}
