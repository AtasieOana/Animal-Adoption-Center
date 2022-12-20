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
public class AnimalDto {

    @NotNull(message = ProjectConstants.ID_NULL)
    private Long id;

    @NotNull(message = ProjectConstants.ANIMAL_TYPE_NULL)
    private String animalType;

    private Integer birthYear;

    private Date foundDate;

    private CageDto cageDto;

    private ClientDto clientDto;

    private DietDto dietDto;
}
