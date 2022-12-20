package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Setter
@Getter
public class VaccineDto {

    @NotNull(message = ProjectConstants.VACCINE_NAME_NULL)
    private String vaccineName;

    @NotNull(message = ProjectConstants.EXP_DATE_NULL)
    private Date expirationDate;

    private Integer quantityOnStock;
}
