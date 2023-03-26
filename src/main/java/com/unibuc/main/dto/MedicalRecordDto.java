package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MedicalRecordDto {

    @NotNull(message = ProjectConstants.HEALTH_NULL)
    private String generalHealthState;

    @NotNull(message = ProjectConstants.GENERATION_DATE_NULL)
    private Date generationDate;

    @NotNull(message = ProjectConstants.ID_NULL)
    private Long animalId;

    @NotNull(message = ProjectConstants.FIRST_NAME_NULL)
    private String vetFirstName;

    @NotNull(message = ProjectConstants.LAST_NAME_NULL)
    private String vetLastName;

}
