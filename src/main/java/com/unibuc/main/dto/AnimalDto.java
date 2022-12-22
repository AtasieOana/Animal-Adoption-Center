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

    private Long id;

    private String animalType;

    private Integer birthYear;

    private Date foundDate;

    private CageDto cageDto;

    private ClientDto clientDto;

    private DietDto dietDto;
}
