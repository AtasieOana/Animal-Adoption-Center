package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.CaretakerService;
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
    public ResponseEntity<List<EmployeeDto>> getAllCaretakers(){
        return ResponseEntity.ok(caretakerService.getAllEmployees());
    }
    
    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<EmployeeDto> getCaretakerByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(caretakerService.getEmployeeByName(firstName, lastName));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> addNewCaretaker(@Valid @RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok(caretakerService.addNewEmployee(employeeDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String firstName, @PathVariable String lastName){
        caretakerService.deleteEmployee(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    public ResponseEntity<EmployeeDto> updateCaretaker(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody EmployeeDto newEmployeeDto){
        return ResponseEntity.ok(caretakerService.updateEmployee(oldFirstName, oldLastName, newEmployeeDto));
    }

    @PutMapping("/updateAllSalaries/{percent}")
    public ResponseEntity<List<EmployeeDto>> updateCaretaker(@PathVariable Integer percent){
        return ResponseEntity.ok(caretakerService.updateAllSalariesWithAPercent(percent));
    }

}
