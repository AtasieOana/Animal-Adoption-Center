package com.unibuc.main.controller;

import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Viewing the vaccines in the order of expiration")
    public ResponseEntity<List<VaccineDto>> getAllVaccinesOrderByExpiredDate(){
        return ResponseEntity.ok(vaccineService.getAllVaccinesOrderByExpiredDate());
    }

    @PostMapping
    @ApiOperation("Addition of a new vaccine characterized by name, quantity brought and expiration date")
    public ResponseEntity<VaccineDto> addNewVaccine(@Valid @RequestBody VaccineDto vaccineDto){
        return ResponseEntity.ok(vaccineService.addVaccine(vaccineDto));
    }

    @DeleteMapping("/deleteExpiredVaccines")
    @ApiOperation("Deleting expired vaccines from the system, equivalent to throwing them in the trash")
    public ResponseEntity<String> deleteExpiredVaccines(){
        return ResponseEntity.ok(vaccineService.deleteExpiredVaccines());
    }

    @GetMapping("/getAllVaccinesWithEmptyStock")
    @ApiOperation("Viewing the vaccines with empty stock, to know what needs to be repurchased")
    public ResponseEntity<List<VaccineDto>> getAllVaccinesWithEmptyStock(){
        return ResponseEntity.ok(vaccineService.getAllVaccinesWithEmptyStock());
    }

    @PutMapping("/{vaccineName}")
    @ApiOperation("Changing the stock and expiration date of an existing vaccine")
    public ResponseEntity<VaccineDto> updateVaccine(@PathVariable String vaccineName, @Valid @RequestBody PartialVaccineDto newVaccine){
        return ResponseEntity.ok(vaccineService.updateVaccine(vaccineName, newVaccine));
    }



}
