package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
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
