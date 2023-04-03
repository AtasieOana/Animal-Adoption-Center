package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundDate;

    private CageDto cageDto;

    private ClientDto clientDto;

    @NotNull(message = ProjectConstants.DIET_NULL)
    private DietDto dietDto;
}
