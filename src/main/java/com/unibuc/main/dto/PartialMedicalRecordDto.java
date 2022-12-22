package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@Setter
@Getter
public class PartialMedicalRecordDto {

    @NotNull(message = ProjectConstants.HEALTH_NULL)
    private String generalHealthState;

    @NotNull(message = ProjectConstants.GENERATION_DATE_NULL)
    private Date generationDate;

}
