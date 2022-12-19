package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class DietDto {

    @NotNull(message = ProjectConstants.DIET_TYPE_NULL)
    private String dietType;

    private Integer quantityOnStock;
}
