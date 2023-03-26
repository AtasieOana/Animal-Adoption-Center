package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.RegisteredVaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/registeredVaccines")
public class RegisteredVaccineController {


    @Autowired
    private RegisteredVaccineService registeredVaccineService;

    @GetMapping()
    public ResponseEntity<List<RegisteredVaccineDto>> getAllRegisteredVaccines(){
        return ResponseEntity.ok(registeredVaccineService.getAllRegisteredVaccines());
    }

    @PostMapping("/{medicalRecordId}/{regDate}")
    public ResponseEntity<List<RegisteredVaccineDto>> associateVaccinesWithMedicalRecord(@Valid @RequestBody List<VaccineDto> vaccineDtos,
                                                                                         @PathVariable Long medicalRecordId,
                                                                                         @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date regDate){
        return ResponseEntity.ok(registeredVaccineService.associateVaccinesWithMedicalRecord(vaccineDtos, medicalRecordId, regDate));
    }

    @PatchMapping("/updateRegistrationDate/{id}/{date}")
    public ResponseEntity<RegisteredVaccineDto> updateRegistrationDate(@PathVariable Long id,
                                                                       @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
            return ResponseEntity.ok(registeredVaccineService.updateRegistrationDate(id, date));
    }

    @DeleteMapping("/{regVaccineId}")
    public ResponseEntity<String> deleteVaccineFromMedicalControl(@PathVariable Long regVaccineId){
        registeredVaccineService.deleteVaccineFromMedicalControl(regVaccineId);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }
}
