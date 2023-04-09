package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddAnimalDto {

    @NotBlank(message = ProjectConstants.ANIMAL_TYPE_BLANK)
    private String animalType;

    @NotNull(message = ProjectConstants.YEAR_NULL)
    @Min(value = 1900, message = ProjectConstants.YEAR_MIN)
    private Integer birthYear;

    @NotNull(message = ProjectConstants.FOUND_DATE_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundDate;

    @NotBlank(message = ProjectConstants.DIET_TYPE_BLANK)
    private String dietType;

    @NotNull(message = ProjectConstants.CAGE_ID_NULL)
    private Long cageId;
}
