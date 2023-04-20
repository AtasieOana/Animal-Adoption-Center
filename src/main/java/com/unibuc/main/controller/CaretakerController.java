package com.unibuc.main.controller;

import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.CaretakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/caretakers")
public class CaretakerController {

    @Autowired
    private CaretakerService caretakerService;

    @RequestMapping("")
    public String getCaretakersPage(Model model,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Page<EmployeeDto> caretakerPage = caretakerService.findPaginatedEmployees(PageRequest.of(currentPage - 1, pageSize), pageSize, currentPage-1);
        model.addAttribute("caretakerPage",caretakerPage);
        return "/employeeTemplates/caretakerPaginated";
    }
    
    @GetMapping("/{firstName}/{lastName}")
    public ModelAndView getCaretakerByName(@PathVariable String firstName, @PathVariable String lastName){
        ModelAndView modelAndView = new ModelAndView("/employeeTemplates/caretakerDetails");
        EmployeeDto caretaker = caretakerService.getEmployeeByName(firstName, lastName);
        modelAndView.addObject("caretaker",caretaker);
        return modelAndView;
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

    @RequestMapping("/delete/{firstName}/{lastName}")
    public String deleteCaretaker(@PathVariable String firstName, @PathVariable String lastName){
        caretakerService.deleteEmployee(firstName, lastName);
        return "redirect:/caretakers";
    }
}
