package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VaccineDto {

    @NotNull(message = ProjectConstants.VACCINE_NAME_NULL)
    private String vaccineName;

    @NotNull(message = ProjectConstants.EXP_DATE_NULL)
    private Date expirationDate;

    @Min(value = 0, message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    private Integer quantityOnStock;
}
