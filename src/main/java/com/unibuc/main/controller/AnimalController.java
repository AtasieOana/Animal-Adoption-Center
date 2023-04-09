package com.unibuc.main.controller;

import com.unibuc.main.dto.AdoptAnimalDto;
import com.unibuc.main.dto.AnimalDto;
import com.unibuc.main.dto.AddAnimalDto;
import com.unibuc.main.repository.ClientRepository;
import com.unibuc.main.service.AnimalService;
import com.unibuc.main.service.CageService;
import com.unibuc.main.service.DietService;
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
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private CageService cageService;

    @Autowired
    private DietService dietService;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("")
    public String getAnimalsPage(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Page<AnimalDto> animalPage = animalService.findPaginatedAnimals(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("animalPage",animalPage);
        return "animalTemplates/animalPaginated";
    }

    /*
    @GetMapping("")
    public ModelAndView animals(){
        ModelAndView modelAndView = new ModelAndView("/animalTemplates/animalList");
        List<AnimalDto> animalList = animalService.getAllAnimals();
        modelAndView.addObject("animals",animalList);
        return modelAndView;
    }
     */
    
    @GetMapping("/{animalId}")
    public ModelAndView getAnimalById(@PathVariable Long animalId){
        ModelAndView modelAndView = new ModelAndView("/animalTemplates/animalDetails");
        AnimalDto animalDto = animalService.getAnimalById(animalId);
        modelAndView.addObject("animalId", animalId);
        modelAndView.addObject("animal", animalDto);
        return modelAndView;
    }

    @GetMapping("/getOldestAnimalInCenter")
    public ModelAndView getOldestAnimalInCenter(){
        ModelAndView modelAndView = new ModelAndView("/animalTemplates/animalDetails");
        AnimalDto animalDto = animalService.getOldestAnimalInCenter();
        modelAndView.addObject("animalId", animalDto.getId());
        modelAndView.addObject("animal", animalDto);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addAnimalForm(Model model) {
        model.addAttribute("animal", new AddAnimalDto());
        model.addAttribute("cagesAll", cageService.getAllCages());
        model.addAttribute("dietsAll", dietService.getAllDiets());
        return "/animalTemplates/addAnimalForm";
    }

    @PostMapping
    public String saveAnimal(@ModelAttribute("animal") @Valid AddAnimalDto addAnimalDto,
                           BindingResult bindingResult, Model model){
        model.addAttribute("cagesAll", cageService.getAllCages());
        model.addAttribute("dietsAll", dietService.getAllDiets());
        if (bindingResult.hasErrors()) {
            return "/animalTemplates/addAnimalForm";
        }
        try{
            animalService.addAnimal(addAnimalDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/animalTemplates/addAnimalForm";
        }
        return "redirect:/animals";
    }

    @RequestMapping("/deleteAdoptedAnimals")
    public String deleteAdoptedAnimals(RedirectAttributes redirectAttributes){
        String result = animalService.deleteAdoptedAnimals();
        redirectAttributes.addAttribute("result", result);
        return "redirect:/animals";
    }

    @RequestMapping("/edit/{animalId}")
    public String editAnimalForm(@PathVariable Long animalId, Model model) {
        AdoptAnimalDto adoptAnimalDto = new AdoptAnimalDto(animalId, null);
        model.addAttribute("animal", adoptAnimalDto);
        model.addAttribute("clientsAll", clientRepository.findAll());
        return "/animalTemplates/editAnimalForm";
    }

    @PostMapping("/adoptAnimal/{animalId}")
    public String adoptAnimal(@PathVariable Long animalId,
                           @ModelAttribute("animal") @Valid AdoptAnimalDto adoptAnimalDto,
                           BindingResult bindingResult, Model model){
        AdoptAnimalDto adoptAnimalDtoEdit = new AdoptAnimalDto(animalId, null);
        model.addAttribute("animal", adoptAnimalDtoEdit);
        model.addAttribute("clientsAll", clientRepository.findAll());
        if (bindingResult.hasErrors()) {
            return "/animalTemplates/editAnimalForm";
        }
        try{
            animalService.adoptAnimal(animalId, adoptAnimalDto.getClientId());
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/animalTemplates/editAnimalForm";
        }
        return "redirect:/animals";
    }

}
