package com.unibuc.main.controller;

import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vaccines")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;
    
    @GetMapping("/getAllVaccinesOrderByExpiredDate")
    public ResponseEntity<List<VaccineDto>> getAllVaccinesOrderByExpiredDate(){
        return ResponseEntity.ok(vaccineService.getAllVaccinesOrderByExpiredDate());
    }

    @PostMapping
    public ResponseEntity<VaccineDto> addNewVaccine(@Valid @RequestBody VaccineDto vaccineDto){
        return ResponseEntity.ok(vaccineService.addVaccine(vaccineDto));
    }

    @DeleteMapping("/deleteExpiredVaccines")
    public ResponseEntity<String> deleteExpiredVaccines(){
        return ResponseEntity.ok(vaccineService.deleteExpiredVaccines());
    }

    @GetMapping("/getAllVaccinesWithEmptyStock")
    public ResponseEntity<List<VaccineDto>> getAllVaccinesWithEmptyStock(){
        return ResponseEntity.ok(vaccineService.getAllVaccinesWithEmptyStock());
    }

    @PutMapping("/{vaccineName}")
    public ResponseEntity<VaccineDto> updateVaccine(@PathVariable String vaccineName, @Valid @RequestBody PartialVaccineDto newVaccine){
        return ResponseEntity.ok(vaccineService.updateVaccine(vaccineName, newVaccine));
    }



}
