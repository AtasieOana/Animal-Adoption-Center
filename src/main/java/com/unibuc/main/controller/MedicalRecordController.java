package com.unibuc.main.controller;

import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/medicalRecords")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView medicalRecords(){
        ModelAndView modelAndView = new ModelAndView("/medicalRecordTemplates/medicalRecordList");
        List<MedicalRecordDto> medicalRecordList = medicalRecordService.getAllMedicalRecords();
        modelAndView.addObject("medicalRecords",medicalRecordList);
        return modelAndView;
    }

    @GetMapping("/{medicalRecordId}")
    public ModelAndView getMedicalRecordById(@PathVariable Long medicalRecordId){
        ModelAndView modelAndView = new ModelAndView("/medicalRecordTemplates/medicalRecordDetails");
        MedicalRecordDto medicalRecordDto = medicalRecordService.getMedicalRecordById(medicalRecordId);
        modelAndView.addObject("medicalRecord", medicalRecordDto);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addMedicalRecordForm(Model model) {
        model.addAttribute("medicalRecord", new AddMedicalRecordDto());
        model.addAttribute("animalsAll", animalRepository.findAllByClientIsNull());
        model.addAttribute("vetsAll", employeeRepository.findAllByExperienceNotNull());
        return "/medicalRecordTemplates/addMedicalRecordForm";
    }

    @PostMapping
    public String saveMedicalRecord(@ModelAttribute("medicalRecord") @Valid AddMedicalRecordDto addMedicalRecordDto,
                           BindingResult bindingResult, Model model){
        model.addAttribute("animalsAll", animalRepository.findAllByClientIsNull());
        model.addAttribute("vetsAll", employeeRepository.findAllByExperienceNotNull());
        if (bindingResult.hasErrors()) {
            return "/medicalRecordTemplates/addMedicalRecordForm";
        }
        try{
            medicalRecordService.addMedicalRecord(addMedicalRecordDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/medicalRecordTemplates/addMedicalRecordForm";
        }
        return "redirect:/medicalRecords";
    }

    @RequestMapping("/edit/{medicalRecordId}")
    public String editMedicalRecordForm(@PathVariable Long medicalRecordId, Model model) {
        model.addAttribute("medicalRecord", medicalRecordService.getMedicalRecordById(medicalRecordId));
        model.addAttribute("animalsAll", animalRepository.findAllByClientIsNull());
        model.addAttribute("vetsAll", employeeRepository.findAllByExperienceNotNull());
        model.addAttribute("medicalRecordId", medicalRecordId);
        return "/medicalRecordTemplates/editMedicalRecordForm";
    }

    @PostMapping("/updateMedicalRecord/{medicalRecordId}")
    public String editMedicalRecord(@PathVariable Long medicalRecordId,
                           @ModelAttribute("medicalRecord") @Valid PartialMedicalRecordDto partialMedicalRecordDto,
                           BindingResult bindingResult, Model model){
        model.addAttribute("caretakersAll",employeeRepository.findAllByResponsibilityNotNull());
        if (bindingResult.hasErrors()) {
            return "/medicalRecordTemplates/editMedicalRecordForm";
        }
        try{
            medicalRecordService.updateMedicalRecord(medicalRecordId, partialMedicalRecordDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/medicalRecordTemplates/editMedicalRecordForm";
        }
        return "redirect:/medicalRecords";
    }

    @RequestMapping("/delete/{medicalRecordId}")
    public String deleteMedicalRecord(@PathVariable Long medicalRecordId){
        medicalRecordService.deleteMedicalRecord(medicalRecordId);
        return "redirect:/medicalRecords";
    }
}
