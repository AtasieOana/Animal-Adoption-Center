package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.VetService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Getting all employees who have the role of vet")
    public ResponseEntity<List<EmployeeDto>> getAllVets(){
        return ResponseEntity.ok(vetService.getAllEmployees());
    }

    @GetMapping("/{firstName}/{lastName}")
    @ApiOperation("Getting a vet by first and last name")
    public ResponseEntity<EmployeeDto> getVetByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(vetService.getEmployeeByName(firstName, lastName));
    }

    @PostMapping
    @ApiOperation("Adding a new vet employee to the system")
    public ResponseEntity<EmployeeDto> addNewVet(@Valid @RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok(vetService.addNewEmployee(employeeDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    @ApiOperation("Delete a vet with a given first and last name")
    public ResponseEntity<String> deleteEmployee(@PathVariable String firstName, @PathVariable String lastName){
        vetService.deleteEmployee(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    @ApiOperation("Update the info of a vet")
    public ResponseEntity<EmployeeDto> updateVet(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody EmployeeDto newEmployeeDto){
        return ResponseEntity.ok(vetService.updateEmployee(oldFirstName, oldLastName, newEmployeeDto));
    }

    @PutMapping("/updateAllSalaries/{percent}")
    @ApiOperation("Increase all vets' salaries by a certain percentage")
    public ResponseEntity<List<EmployeeDto>> updateAllSalariesWithAPercent(@PathVariable Integer percent){
        return ResponseEntity.ok(vetService.updateAllSalariesWithAPercent(percent));
    }

}
