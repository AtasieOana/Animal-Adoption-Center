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
public class PartialVaccineDto {

    private Date expirationDate;

    private Integer quantityOnStock;
}
