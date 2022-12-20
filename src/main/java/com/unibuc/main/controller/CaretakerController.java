package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caretakers")
public class CaretakerController {

    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllCaretakers(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    
    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<EmployeeDto> getCaretakerByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(employeeService.getEmployeeByName(firstName, lastName));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> addNewCaretaker(@RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok(employeeService.addNewEmployee(employeeDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String firstName, @PathVariable String lastName){
        employeeService.deleteEmployee(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    public ResponseEntity<EmployeeDto> updateCaretaker(@PathVariable String oldFirstName, @PathVariable String oldLastName, @RequestBody EmployeeDto newEmployeeDto){
        return ResponseEntity.ok(employeeService.updateEmployee(oldFirstName, oldLastName, newEmployeeDto));
    }

    @PutMapping("/updateAllSalaries/{percent}")
    public ResponseEntity<List<EmployeeDto>> updateCaretaker(@PathVariable Integer percent){
        return ResponseEntity.ok(employeeService.updateAllSalariesWithAPercent(percent));
    }

}
