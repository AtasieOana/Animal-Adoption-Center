package com.unibuc.main.controller;

import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.service.RegisteredVaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/registeredVaccines")
public class RegisteredVaccineController {

    @Autowired
    private RegisteredVaccineService registeredVaccineService;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private RegisteredVaccineMapper registeredVaccineMapper;

    @GetMapping("")
    public ModelAndView registeredVaccines(){
        ModelAndView modelAndView = new ModelAndView("/registeredVaccineTemplates/registeredVaccineList");
        List<RegisteredVaccineDto> registeredVaccineList = registeredVaccineService.getAllRegisteredVaccines();
        modelAndView.addObject("registeredVaccines",registeredVaccineList);
        return modelAndView;
    }

    @GetMapping("/{medicalRecordId}")
    public ModelAndView getRegisteredVaccineByMedicalRecordId(@PathVariable Long medicalRecordId){
        ModelAndView modelAndView = new ModelAndView("/registeredVaccineTemplates/registeredVaccineDetails");
        RegisteredVaccineDto registeredVaccineDto = registeredVaccineService.getRegisteredVaccineByMedicalRecordId(medicalRecordId);
        modelAndView.addObject("registeredVaccine", registeredVaccineDto);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addRegisteredVaccineForm(Model model) {
        model.addAttribute("registeredVaccine", new PartialRegisteredVaccineDto());
        model.addAttribute("vaccinesAll", vaccineRepository.findAll());
        model.addAttribute("medicalRecordsAll", medicalRecordRepository.findAll());
        return "/registeredVaccineTemplates/addRegisteredVaccineForm";
    }

    @PostMapping
    public String saveRegisteredVaccine(@ModelAttribute("registeredVaccine") @Valid PartialRegisteredVaccineDto partialRegisteredVaccineDto,
                           BindingResult bindingResult, Model model){
        model.addAttribute("vaccinesAll", vaccineRepository.findAll());
        model.addAttribute("medicalRecordsAll", medicalRecordRepository.findAll());
        if (bindingResult.hasErrors()) {
            return "/registeredVaccineTemplates/addRegisteredVaccineForm";
        }
        try{
            registeredVaccineService.associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/registeredVaccineTemplates/addRegisteredVaccineForm";
        }
        return "redirect:/registeredVaccines";
    }

    @RequestMapping("/edit/{medicalRecordId}")
    public String editRegisteredVaccineForm(@PathVariable Long medicalRecordId, Model model) {
        model.addAttribute("registeredVaccine", registeredVaccineMapper.mapDtoToPartial(registeredVaccineService.getRegisteredVaccineByMedicalRecordId(medicalRecordId)));
        model.addAttribute("vaccinesAll", vaccineRepository.findAll());
        model.addAttribute("medicalRecordsAll", medicalRecordRepository.findAll());
        return "/registeredVaccineTemplates/editRegisteredVaccineForm";
    }

    @PostMapping("/updateRegisteredVaccine/{medicalRecordId}")
    public String editRegisteredVaccine(@PathVariable Long medicalRecordId,
                           @ModelAttribute("registeredVaccine") @Valid PartialRegisteredVaccineDto partialRegisteredVaccineDto,
                           BindingResult bindingResult, Model model){

        model.addAttribute("vaccinesAll", vaccineRepository.findAll());
        model.addAttribute("medicalRecordsAll", medicalRecordRepository.findAll());
        if (bindingResult.hasErrors()) {
            return "/registeredVaccineTemplates/editRegisteredVaccineForm";
        }
        try{
            registeredVaccineService.updateVaccinesWithMedicalRecord(medicalRecordId, partialRegisteredVaccineDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/registeredVaccineTemplates/editRegisteredVaccineForm";
        }
        return "redirect:/registeredVaccines";
    }

    @RequestMapping("/delete/{medicalRecordId}")
    public String deleteRegisteredVaccine(@PathVariable Long medicalRecordId){
        registeredVaccineService.deleteVaccinesFromMedicalRecord(medicalRecordId);
        return "redirect:/registeredVaccines";
    }
}
