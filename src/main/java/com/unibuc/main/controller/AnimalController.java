package com.unibuc.main.controller;

import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.PartialAnimalDto;
import com.unibuc.main.service.AnimalService;
//import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private AnimalService animalService;
    
    @GetMapping()
    public ResponseEntity<List<AnimalDto>> getAllAnimals(){
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    @GetMapping("/inCage/{cageId}")
   // @ApiOperation(value="Getting all animals from a cage")
    public ResponseEntity<List<AnimalDto>> showAnimalsInCage(@PathVariable Long cageId){
        return ResponseEntity.ok(animalService.showAnimalsInCage(cageId));
    }

    @GetMapping("/getOldestAnimalInCenter")
    public ResponseEntity<AnimalDto> getOldestAnimalInCenter(){
        return ResponseEntity.ok(animalService.getOldestAnimalInCenter());
    }

    @PostMapping
    public ResponseEntity<AnimalDto> addNewAnimal(@Valid @RequestBody PartialAnimalDto partialAnimalDto){
        return ResponseEntity.ok(animalService.addAnimal(partialAnimalDto));
    }

    @DeleteMapping("/deleteAdoptedAnimals")
    public ResponseEntity<String> deleteAdoptedAnimals(){
        return ResponseEntity.ok(animalService.deleteAdoptedAnimals());
    }

    @PutMapping("/adoptAnimal/{animalId}/{clientFirstName}/{clientLastName}")
    public ResponseEntity<AnimalDto> adoptAnimal(@PathVariable Long animalId, @PathVariable String clientFirstName, @PathVariable String clientLastName){
        return ResponseEntity.ok(animalService.adoptAnimal(animalId, clientFirstName, clientLastName));
    }

    @PutMapping("/putAnimalInCage/{animalId}/{cageId}")
    public ResponseEntity<AnimalDto> putAnimalInCage(@PathVariable Long animalId, @PathVariable Long cageId){
        return ResponseEntity.ok(animalService.putAnimalInCage(animalId, cageId));
    }

}
