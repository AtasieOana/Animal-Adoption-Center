package com.unibuc.main.controller;

import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.MedicalRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/medicalRecords")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @GetMapping("/getMedicalRecordsForAnimal/{animalId}")
    @ApiOperation("Viewing all the medical records associated with an animal")
    public ResponseEntity<List<MedicalRecordDto>> getAllMedicalRecordsForAnimal(@PathVariable Long animalId){
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecordsForAnimal(animalId));
    }

    @PostMapping
    @ApiOperation("Saving a new medical record")
    public ResponseEntity<MedicalRecordDto> addNewMedicalRecord(@Valid @RequestBody MedicalRecordDto medicalRecordDto){
        return ResponseEntity.ok(medicalRecordService.addMedicalRecord(medicalRecordDto));
    }

    @DeleteMapping("/deleteMedicalRecordBeforeDate/{date}")
    @ApiOperation("Deleting medical records prior to a date to avoid retaining information that is no longer relevant")
    public ResponseEntity<String> deleteMedicalRecordBeforeADate(@PathVariable String date){
        return ResponseEntity.ok(medicalRecordService.deleteMedicalRecordBeforeADate(date));
    }

    @PutMapping("/{id}")
    @ApiOperation("If the same vaccine is repeated on an animal, only the state of health and the generation date of the medical record can be updated")
    public ResponseEntity<MedicalRecordDto> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody PartialMedicalRecordDto newMedicalRecord){
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, newMedicalRecord));
    }



}
