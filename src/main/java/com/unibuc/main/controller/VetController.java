package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vets")
public class VetController {

    @Autowired
    private VetService vetService;
    
    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllVets(){
        return ResponseEntity.ok(vetService.getAllEmployees());
    }
    
    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<EmployeeDto> getVetByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(vetService.getEmployeeByName(firstName, lastName));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> addNewVet(@Valid @RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok(vetService.addNewEmployee(employeeDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String firstName, @PathVariable String lastName){
        vetService.deleteEmployee(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    public ResponseEntity<EmployeeDto> updateVet(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody EmployeeDto newEmployeeDto){
        return ResponseEntity.ok(vetService.updateEmployee(oldFirstName, oldLastName, newEmployeeDto));
    }

    @PutMapping("/updateAllSalaries/{percent}")
    public ResponseEntity<List<EmployeeDto>> updateAllSalariesWithAPercent(@PathVariable Integer percent){
        return ResponseEntity.ok(vetService.updateAllSalariesWithAPercent(percent));
    }

}
