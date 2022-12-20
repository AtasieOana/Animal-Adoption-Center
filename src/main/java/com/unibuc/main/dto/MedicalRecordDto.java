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
public class MedicalRecordDto {

    @NotNull(message = ProjectConstants.ID_NULL)
    private Long id;

    @NotNull(message = ProjectConstants.HEALTH_NULL)
    private String generalHealthState;

    private Date generationDate;

    private AnimalDto animalDto;

    private VaccineDto vaccineDto;

    private EmployeeDto vetDto;
}
