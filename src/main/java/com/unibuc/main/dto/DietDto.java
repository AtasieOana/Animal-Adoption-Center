package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DietDto {

    @NotBlank(message = ProjectConstants.DIET_TYPE_BLANK)
    private String dietType;

    @NotNull(message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    @Min(value = 0, message = ProjectConstants.NR_QUANTITY_NEGATIVE)
    private Integer quantityOnStock;
}
