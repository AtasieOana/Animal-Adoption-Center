package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.service.CageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cages")
public class CageController {

    @Autowired
    private CageService cageService;
    
    @GetMapping("/getCagesWithoutACaretaker")
    public ResponseEntity<List<CageDto>> getCagesWithoutACaretaker(){
        return ResponseEntity.ok(cageService.getCagesWithoutACaretaker());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CageDto> getCageById(@PathVariable Long id){
        return ResponseEntity.ok(cageService.getCageById(id));
    }

    @PostMapping
    public ResponseEntity<CageDto> addNewCage(@Valid @RequestBody CageDto cageDto){
        return ResponseEntity.ok(cageService.addCage(cageDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCage(@PathVariable Long id){
        cageService.deleteCage(id);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PatchMapping("/updateCageCaretaker/{id}/{caretakerFirstName}/{caretakerLastName}")
    public ResponseEntity<CageDto> updateCageCaretaker(@PathVariable Long id, @PathVariable String caretakerFirstName, @PathVariable String caretakerLastName){
        return ResponseEntity.ok(cageService.updateCageCaretaker(id, caretakerFirstName, caretakerLastName));
    }

    @PatchMapping("/updateCagePlaces/{id}/{nrPlaces}")
    public ResponseEntity<CageDto> updateCage(@PathVariable Long id, @PathVariable Integer nrPlaces){
        return ResponseEntity.ok(cageService.updatePlacesInCage(id, nrPlaces));
    }

}
