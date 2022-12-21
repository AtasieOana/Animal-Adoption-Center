package com.unibuc.main.controller;

import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/diets")
public class DietController {

    @Autowired
    private DietService dietService;
    
    @GetMapping("/getAllDiets")
    public ResponseEntity<List<DietDto>> getAllDiets(){
        return ResponseEntity.ok(dietService.getAllDiets());
    }

    @PostMapping
    public ResponseEntity<DietDto> addNewDiet(@Valid @RequestBody DietDto dietDto){
        return ResponseEntity.ok(dietService.addDiet(dietDto));
    }

    @DeleteMapping("/deleteIfStockEmpty/{dietType}")
    public ResponseEntity<String> deleteDietOnlyIfStockEmpty(@PathVariable String dietType){
        return ResponseEntity.ok(dietService.deleteDietOnlyIfStockEmpty(dietType));
    }

    @GetMapping("/getAllDietsWithEmptyStock")
    public ResponseEntity<List<DietDto>> getAllDietsWithEmptyStock(){
        return ResponseEntity.ok(dietService.getAllDietsWithEmptyStock());
    }

    @PutMapping("/{dietType}")
    public ResponseEntity<DietDto> updateDiet(@PathVariable String dietType, @RequestBody DietDto newDiet){
        return ResponseEntity.ok(dietService.updateDietPartial(dietType, newDiet));
    }



}
