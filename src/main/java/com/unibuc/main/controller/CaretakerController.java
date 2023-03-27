package com.unibuc.main.controller;

import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.CaretakerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/caretakers")
public class CaretakerController {

    @Autowired
    private CaretakerService caretakerService;

    @GetMapping("")
    public ModelAndView getAllCaretakers(){
        ModelAndView modelAndView = new ModelAndView("/employeeTemplates/caretakerList");
        List<EmployeeDto> caretakers = caretakerService.getAllEmployees();
        modelAndView.addObject("caretakers",caretakers);
        return modelAndView;
    }

    @GetMapping("/{firstName}/{lastName}")
    public ModelAndView getCaretakerByName(@PathVariable String firstName, @PathVariable String lastName){
        ModelAndView modelAndView = new ModelAndView("/employeeTemplates/caretakerDetails");
        EmployeeDto caretaker = caretakerService.getEmployeeByName(firstName, lastName);
        modelAndView.addObject("caretaker",caretaker);
        return modelAndView;
    }

    @RequestMapping("/delete/{firstName}/{lastName}")
    public String deleteCaretaker(@PathVariable String firstName, @PathVariable String lastName){
        caretakerService.deleteEmployee(firstName, lastName);
        return "redirect:/caretakers";
    }

    @RequestMapping("/add")
    public String addCaretakerForm(Model model) {
        model.addAttribute("caretaker", new EmployeeDto());
        return "/employeeTemplates/addCaretakerForm";
    }

    @PostMapping
    public String saveCaretaker(@ModelAttribute("caretaker") @Valid EmployeeDto employeeDto,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/employeeTemplates/addCaretakerForm";
        }
        try{
            caretakerService.addNewEmployee(employeeDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/employeeTemplates/addCaretakerForm";
        }
        return "redirect:/caretakers";
    }

    @RequestMapping("/edit/{oldFirstName}/{oldLastName}")
    public String editCaretakerForm(@PathVariable String oldFirstName, @PathVariable String oldLastName, Model model) {
        model.addAttribute("caretaker", caretakerService.getEmployeeByName(oldFirstName, oldLastName));
        model.addAttribute("oldFirstName", oldFirstName);
        model.addAttribute("oldLastName", oldLastName);
        return "/employeeTemplates/editCaretakerForm";
    }

    @PostMapping("/updateCaretaker/{oldFirstName}/{oldLastName}")
    public String editCaretaker(@PathVariable String oldFirstName, @PathVariable String oldLastName,
                                @ModelAttribute("caretaker") @Valid EmployeeDto employeeDto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/employeeTemplates/editCaretakerForm";
        }
        try{
            caretakerService.updateEmployee(oldFirstName, oldLastName, employeeDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/employeeTemplates/editCaretakerForm";
        }
        return "redirect:/caretakers";
    }
    /*
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

    @DeleteMapping("/{firstName}/{lastName}")
    @ApiOperation("Delete a caretaker with a given first and last name")
    public ResponseEntity<String> deleteEmployee(@PathVariable String firstName, @PathVariable String lastName){
        caretakerService.deleteEmployee(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PostMapping
    @ApiOperation("Adding a new caretaker employee to the system")
    public ResponseEntity<EmployeeDto> addNewCaretaker(@Valid @RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok(caretakerService.addNewEmployee(employeeDto));
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    @ApiOperation("Update the info of a caretaker")
    public ResponseEntity<EmployeeDto> updateCaretaker(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody EmployeeDto newEmployeeDto){
        return ResponseEntity.ok(caretakerService.updateEmployee(oldFirstName, oldLastName, newEmployeeDto));
    }
    */

}
