package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PartialVaccineDto {

    private Date expirationDate;

    @Min(value = 0, message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    private Integer quantityOnStock;
}
