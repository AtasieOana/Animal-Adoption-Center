package com.unibuc.main.controller;

import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.CageService;
import com.unibuc.main.service.employees.CaretakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cages")
public class CageController {

    @Autowired
    private CageService cageService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView cages(){
        ModelAndView modelAndView = new ModelAndView("/cageTemplates/cageList");
        List<CageDto> cageList = cageService.getAllCages();
        modelAndView.addObject("cages",cageList);
        return modelAndView;
    }

    @GetMapping("/{cageId}")
    public ModelAndView getCageById(@PathVariable Long cageId){
        ModelAndView modelAndView = new ModelAndView("/cageTemplates/cageDetails");
        CageDto cageDto = cageService.getCageById(cageId);
        modelAndView.addObject("cageId", cageId);
        modelAndView.addObject("cage", cageDto);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addCageForm(Model model) {
        model.addAttribute("cage", new PartialCageDto());
        model.addAttribute("caretakersAll",employeeRepository.findAllByResponsibilityNotNull());
        return "/cageTemplates/addCageForm";
    }

    @PostMapping
    public String saveCage(@ModelAttribute("cage") @Valid PartialCageDto partialCageDto,
                           BindingResult bindingResult, Model model){
        model.addAttribute("caretakersAll",employeeRepository.findAllByResponsibilityNotNull());
        if (bindingResult.hasErrors()) {
            return "/cageTemplates/addCageForm";
        }
        try{
            cageService.addCage(partialCageDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/cageTemplates/addCageForm";
        }
        return "redirect:/cages";
    }

    @RequestMapping("/edit/{cageId}")
    public String editCageForm(@PathVariable Long cageId, Model model) {
        model.addAttribute("cage", cageService.getPartialCageById(cageId));
        model.addAttribute("caretakersAll",employeeRepository.findAllByResponsibilityNotNull());
        return "/cageTemplates/editCageForm";
    }

    @PostMapping("/updateDiet/{cageId}")
    public String editCage(@PathVariable Long cageId,
                           @ModelAttribute("cage") @Valid PartialCageDto partialCageDto,
                           BindingResult bindingResult, Model model){
        model.addAttribute("caretakersAll",employeeRepository.findAllByResponsibilityNotNull());
        if (bindingResult.hasErrors()) {
            return "/cageTemplates/editCageForm";
        }
        try{
            cageService.updateCage(cageId, partialCageDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/cageTemplates/editCageForm";
        }
        return "redirect:/cages";
    }

    @RequestMapping("/delete/{cageId}")
    public String deleteDietIfStockEmpty(@PathVariable Long cageId){
        cageService.deleteCage(cageId);
        return "redirect:/cages";
    }
}
