package com.unibuc.main.dto;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.entity.Vet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Setter
@Getter
public class MedicalRecordDto {

    @NotNull(message = ProjectConstants.ID_NULL)
    private Long id;

    @NotNull(message = ProjectConstants.HEALTH_NULL)
    private String generalHealthState;

    private Date generationDate;

    private AnimalDto animalDto;

    private VaccineDto vaccineDto;

    private VetDto vetDto;
}
