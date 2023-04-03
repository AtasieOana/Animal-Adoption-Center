package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VaccineDto {

    @NotBlank(message = ProjectConstants.VACCINE_NAME_BLANK)
    @Size(min = 5, message = ProjectConstants.VACCINE_NAME_SIZE)
    private String vaccineName;

    @NotNull(message = ProjectConstants.EXP_DATE_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    @Min(value = 0, message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    @NotNull(message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    private Integer quantityOnStock;
}
