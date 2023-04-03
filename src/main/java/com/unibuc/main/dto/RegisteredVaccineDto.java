package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisteredVaccineDto {

    @Size(min=1, max=3, message = ProjectConstants.VACCINE_LIST_SIZE)
    private List<VaccineDto> vaccinesDto;

    @NotNull(message = ProjectConstants.ID_NULL)
    private MedicalRecordDto medicalRecordDto;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = ProjectConstants.REG_DATE_NULL)
    private Date registrationDate;
}
