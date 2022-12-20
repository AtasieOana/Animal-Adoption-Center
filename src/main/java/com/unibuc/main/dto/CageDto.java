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
public class CageDto {

    @NotNull(message = ProjectConstants.ID_NULL)
    Long id;

    @NotNull(message = ProjectConstants.NR_PLACES_NULL)
    private Integer numberPlaces;

    private EmployeeDto caretakerDto;

}
