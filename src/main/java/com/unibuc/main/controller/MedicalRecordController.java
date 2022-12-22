package com.unibuc.main.controller;

import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.MedicalRecordService;
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
    public ResponseEntity<List<MedicalRecordDto>> getAllMedicalRecordsForAnimal(@PathVariable Long animalId){
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecordsForAnimal(animalId));
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDto> addNewMedicalRecord(@Valid @RequestBody MedicalRecordDto medicalRecordDto){
        return ResponseEntity.ok(medicalRecordService.addMedicalRecord(medicalRecordDto));
    }

    @DeleteMapping("/deleteMedicalRecordBeforeDate/{date}")
    public ResponseEntity<String> deleteMedicalRecordBeforeADate(@PathVariable String date){
        return ResponseEntity.ok(medicalRecordService.deleteMedicalRecordBeforeADate(date));
    }

    @GetMapping("/getMostUsedVaccine")
    public ResponseEntity<VaccineDto> getMostUsedVaccine(){
        return ResponseEntity.ok(medicalRecordService.getMostUsedVaccine());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody PartialMedicalRecordDto newMedicalRecord){
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, newMedicalRecord));
    }



}
