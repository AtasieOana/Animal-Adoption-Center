package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PartialMedicalRecordDto {

    @NotNull(message = ProjectConstants.HEALTH_NULL)
    private String generalHealthState;

    @NotNull(message = ProjectConstants.GENERATION_DATE_NULL)
    private Date generationDate;

}
