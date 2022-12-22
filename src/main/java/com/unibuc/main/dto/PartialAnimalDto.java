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
public class PartialAnimalDto {

    @NotNull(message = ProjectConstants.ANIMAL_TYPE_NULL)
    private String animalType;

    @NotNull(message = ProjectConstants.YEAR_NULL)
    private Integer birthYear;

    @NotNull(message = ProjectConstants.FOUND_DATE_NULL)
    private Date foundDate;

    @NotNull(message = ProjectConstants.DIET_TYPE_NULL)
    private String dietType;
}
