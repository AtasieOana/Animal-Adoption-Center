package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.CaretakerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/caretakers")
public class CaretakerController {

    @Autowired
    private CaretakerService caretakerService;
    
    @GetMapping()
    @ApiOperation("Getting all employees who have the role of caretaker")
    public ResponseEntity<List<EmployeeDto>> getAllCaretakers(){
        return ResponseEntity.ok(caretakerService.getAllEmployees());
    }
    
    @GetMapping("/{firstName}/{lastName}")
    @ApiOperation("Getting a caretaker by first and last name")
    public ResponseEntity<EmployeeDto> getCaretakerByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(caretakerService.getEmployeeByName(firstName, lastName));
    }

    @PostMapping
    @ApiOperation("Adding a new caretaker employee to the system")
    public ResponseEntity<EmployeeDto> addNewCaretaker(@Valid @RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok(caretakerService.addNewEmployee(employeeDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    @ApiOperation("Delete a caretaker with a given first and last name")
    public ResponseEntity<String> deleteEmployee(@PathVariable String firstName, @PathVariable String lastName){
        caretakerService.deleteEmployee(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    @ApiOperation("Update the info of a caretaker")
    public ResponseEntity<EmployeeDto> updateCaretaker(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody EmployeeDto newEmployeeDto){
        return ResponseEntity.ok(caretakerService.updateEmployee(oldFirstName, oldLastName, newEmployeeDto));
    }

    @PutMapping("/updateAllSalaries/{percent}")
    @ApiOperation("Increase all caretakers' salaries by a certain percentage")
    public ResponseEntity<List<EmployeeDto>> updateAllSalariesWithAPercent(@PathVariable Integer percent){
        return ResponseEntity.ok(caretakerService.updateAllSalariesWithAPercent(percent));
    }

}
