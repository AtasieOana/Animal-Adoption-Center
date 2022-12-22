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

    @NotNull(message = ProjectConstants.HEALTH_NULL)
    private String generalHealthState;

    @NotNull(message = ProjectConstants.GENERATION_DATE_NULL)
    private Date generationDate;

    @NotNull(message = ProjectConstants.ID_NULL)
    private Long animalId;

    @NotNull(message = ProjectConstants.VACCINE_NAME_NULL)
    private String vaccineName;

    @NotNull(message = ProjectConstants.FIRST_NAME_NULL)
    private String vetFirstName;

    @NotNull(message = ProjectConstants.LAST_NAME_NULL)
    private String vetLastName;

}
