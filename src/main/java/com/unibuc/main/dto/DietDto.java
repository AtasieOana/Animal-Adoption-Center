package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DietDto {

    @NotNull(message = ProjectConstants.DIET_TYPE_NULL)
    private String dietType;

    @Min(value = 0, message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    private Integer quantityOnStock;
}
