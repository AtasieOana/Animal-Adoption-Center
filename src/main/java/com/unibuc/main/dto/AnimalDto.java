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
public class AnimalDto {

    private Long id;

    private String animalType;

    private Integer birthYear;

    private Date foundDate;

    private CageDto cageDto;

    private ClientDto clientDto;

    private DietDto dietDto;
}
