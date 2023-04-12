package com.unibuc.main.controller;

import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/vaccines")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;
    @RequestMapping("")
    public String getVaccinesPage(Model model,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<VaccineDto> vaccinePage = vaccineService.findPaginatedVaccines(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("vaccinePage",vaccinePage);
        return "/vaccineTemplates/vaccinePaginated";
    }

    @GetMapping("/{vaccineType}")
    public ModelAndView getVaccineByName(@PathVariable String vaccineType){
        ModelAndView modelAndView = new ModelAndView("/vaccineTemplates/vaccineDetails");
        VaccineDto vaccineDto = vaccineService.getVaccineByName(vaccineType);
        modelAndView.addObject("vaccine", vaccineDto);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addVaccineForm(Model model) {
        model.addAttribute("vaccine", new VaccineDto());
        return "/vaccineTemplates/addVaccineForm";
    }

    @PostMapping
    public String saveVaccine(@ModelAttribute("vaccine") @Valid VaccineDto vaccineDto,
                           BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/vaccineTemplates/addVaccineForm";
        }
        try{
            vaccineService.addVaccine(vaccineDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/vaccineTemplates/addVaccineForm";
        }
        return "redirect:/vaccines";
    }

    @RequestMapping("/edit/{oldVaccineName}")
    public String editVaccineForm(@PathVariable String oldVaccineName, Model model) {
        model.addAttribute("vaccine", vaccineService.getVaccineByName(oldVaccineName));
        model.addAttribute("oldVaccineName", oldVaccineName);
        return "/vaccineTemplates/editVaccineForm";
    }

    @PostMapping("/updateVaccine/{oldVaccineName}")
    public String editVaccine(@PathVariable String oldVaccineName,
                                @ModelAttribute("vaccine") @Valid PartialVaccineDto partialVaccineDto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/vaccineTemplates/editVaccineForm";
        }
        try{
            vaccineService.updateVaccine(oldVaccineName, partialVaccineDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/vaccineTemplates/editVaccineForm";
        }
        return "redirect:/vaccines";
    }

    @RequestMapping("/deleteExpiredVaccines")
    public String deleteExpiredVaccines(RedirectAttributes redirectAttributes){
        String result = vaccineService.deleteExpiredVaccines();
        redirectAttributes.addAttribute("result", result);
        return "redirect:/vaccines";
    }
}
