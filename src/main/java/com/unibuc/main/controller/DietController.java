package com.unibuc.main.controller;

import com.unibuc.main.dto.DietDto;
import com.unibuc.main.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/diets")
public class DietController {

    @Autowired
    private DietService dietService;

    @GetMapping("")
    public ModelAndView getAllDiets(){
        ModelAndView modelAndView = new ModelAndView("/dietTemplates/dietList");
        List<DietDto> diets = dietService.getAllDiets();
        modelAndView.addObject("diets",diets);
        return modelAndView;
    }

    @GetMapping("/{dietType}")
    public ModelAndView getDietByType(@PathVariable String dietType){
        ModelAndView modelAndView = new ModelAndView("/dietTemplates/dietDetails");
        DietDto dietDto = dietService.getDietByType(dietType);
        modelAndView.addObject("diet", dietDto);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addDietForm(Model model) {
        model.addAttribute("diet", new DietDto());
        return "/dietTemplates/addDietForm";
    }

    @PostMapping
    public String saveDiet(@ModelAttribute("diet") @Valid DietDto dietDto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/dietTemplates/addDietForm";
        }
        try{
            dietService.addDiet(dietDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/dietTemplates/addDietForm";
        }
        return "redirect:/diets";
    }

    @RequestMapping("/edit/{oldDietType}")
    public String editDietForm(@PathVariable String oldDietType, Model model) {
        model.addAttribute("diet", dietService.getDietByType(oldDietType));
        model.addAttribute("oldDietType", oldDietType);
        return "/dietTemplates/editDietForm";
    }

    @PostMapping("/updateDiet/{oldDietType}")
    public String editCaretaker(@PathVariable String oldDietType,
                                @ModelAttribute("diet") @Valid DietDto dietDto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/dietTemplates/editDietForm";
        }
        try{
            dietService.updateDietPartial(oldDietType, dietDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/dietTemplates/editDietForm";
        }
        return "redirect:/diets";
    }

    @RequestMapping("/deleteIfStockEmpty/{dietType}")
    public String deleteDietIfStockEmpty(@PathVariable String dietType,  RedirectAttributes redirectAttributes){
        String result = dietService.deleteDietOnlyIfStockEmpty(dietType);
        redirectAttributes.addAttribute("result", result);
        return "redirect:/diets";
    }
}
