package com.unibuc.main.controller;

import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/vets")
public class VetController {

    @Autowired
    private VetService vetService;

    @GetMapping("")
    public ModelAndView getAllVets(){
        ModelAndView modelAndView = new ModelAndView("/employeeTemplates/vetList");
        List<EmployeeDto> vets = vetService.getAllEmployees();
        modelAndView.addObject("vets",vets);
        return modelAndView;
    }

    @GetMapping("/{firstName}/{lastName}")
    public ModelAndView getVetByName(@PathVariable String firstName, @PathVariable String lastName){
        ModelAndView modelAndView = new ModelAndView("/employeeTemplates/vetDetails");
        EmployeeDto vet = vetService.getEmployeeByName(firstName, lastName);
        modelAndView.addObject("vet",vet);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addVetForm(Model model) {
        model.addAttribute("vet", new EmployeeDto());
        return "/employeeTemplates/addVetForm";
    }

    @PostMapping
    public String saveVet(@ModelAttribute("vet") @Valid EmployeeDto employeeDto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/employeeTemplates/addVetForm";
        }
        try{
            vetService.addNewEmployee(employeeDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/employeeTemplates/addVetForm";
        }
        return "redirect:/vets";
    }

    @RequestMapping("/edit/{oldFirstName}/{oldLastName}")
    public String editVetForm(@PathVariable String oldFirstName, @PathVariable String oldLastName, Model model) {
        model.addAttribute("vet", vetService.getEmployeeByName(oldFirstName, oldLastName));
        model.addAttribute("oldFirstName", oldFirstName);
        model.addAttribute("oldLastName", oldLastName);
        return "/employeeTemplates/editVetForm";
    }

    @PostMapping("/updateVet/{oldFirstName}/{oldLastName}")
    public String editVet(@PathVariable String oldFirstName, @PathVariable String oldLastName,
                                @ModelAttribute("vet") @Valid EmployeeDto employeeDto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/employeeTemplates/editVetForm";
        }
        try{
            vetService.updateEmployee(oldFirstName, oldLastName, employeeDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/employeeTemplates/editVetForm";
        }
        return "redirect:/vets";
    }

    @RequestMapping("/delete/{firstName}/{lastName}")
    public String deleteVet(@PathVariable String firstName, @PathVariable String lastName){
        vetService.deleteEmployee(firstName, lastName);
        return "redirect:/vets";
    }
}
