package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class CageDto {

    Long id;

    @NotNull(message = ProjectConstants.NR_PLACES_NULL)
    @Min(value = 1, message = ProjectConstants.NR_PLACES_NEGATIVE)
    private Integer numberPlaces;

    private EmployeeDto caretaker;

}
