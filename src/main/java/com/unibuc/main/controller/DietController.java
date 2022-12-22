package com.unibuc.main.controller;

import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Viewing all the diets bought")
    public ResponseEntity<List<DietDto>> getAllDiets(){
        return ResponseEntity.ok(dietService.getAllDiets());
    }

    @PostMapping
    @ApiOperation("Addition of a new diet characterized by type and quantity brought")
    public ResponseEntity<DietDto> addNewDiet(@Valid @RequestBody DietDto dietDto){
        return ResponseEntity.ok(dietService.addDiet(dietDto));
    }

    @DeleteMapping("/deleteIfStockEmpty/{dietType}")
    @ApiOperation("Deleting a useless diet type, this operation can only be performed if the stock is empty")
    public ResponseEntity<String> deleteDietOnlyIfStockEmpty(@PathVariable String dietType){
        return ResponseEntity.ok(dietService.deleteDietOnlyIfStockEmpty(dietType));
    }

    @GetMapping("/getAllDietsWithEmptyStock")
    @ApiOperation("Viewing the diets with empty stock")
    public ResponseEntity<List<DietDto>> getAllDietsWithEmptyStock(){
        return ResponseEntity.ok(dietService.getAllDietsWithEmptyStock());
    }

    @PutMapping("/{dietType}")
    @ApiOperation("Updating the info about a specific diet")
    public ResponseEntity<DietDto> updateDiet(@PathVariable String dietType, @RequestBody DietDto newDiet){
        return ResponseEntity.ok(dietService.updateDietPartial(dietType, newDiet));
    }



}
