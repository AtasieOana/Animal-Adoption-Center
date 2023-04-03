package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MedicalRecordDto {

    private Long id;

    @NotBlank(message = ProjectConstants.HEALTH_BLANK)
    private String generalHealthState;

    @NotNull(message = ProjectConstants.GENERATION_DATE_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date generationDate;

    @NotNull(message = ProjectConstants.ID_NULL)
    private Long animalId;

    @NotBlank(message = ProjectConstants.FIRST_NAME_BLANK)
    private String vetFirstName;

    @NotBlank(message = ProjectConstants.LAST_NAME_BLANK)
    private String vetLastName;

}
