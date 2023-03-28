package com.unibuc.main.controller;

import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("")
    public ModelAndView getAllClients(){
        ModelAndView modelAndView = new ModelAndView("clientTemplates/clientList");
        List<ClientDto> clientList = clientService.getAllClients();
        modelAndView.addObject("clients",clientList);

        Integer avgAge = clientService.getAvgAgeForClient();
        modelAndView.addObject("avgAge",avgAge);
        return modelAndView;
    }

    @GetMapping("/{firstName}/{lastName}")
    public ModelAndView getClientByName(@PathVariable String firstName, @PathVariable String lastName){
        ModelAndView modelAndView = new ModelAndView("/clientTemplates/clientDetails");
        ClientDto client = clientService.getClientByName(firstName, lastName);
        modelAndView.addObject("client",client);
        return modelAndView;
    }

    @RequestMapping("/add")
    public String addClientForm(Model model) {
        model.addAttribute("client", new ClientDto());
        return "/clientTemplates/addClientForm";
    }

    @PostMapping
    public String saveClient(@ModelAttribute("client") @Valid ClientDto clientDto,
                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/clientTemplates/addClientForm";
        }
        try{
            clientService.addNewClient(clientDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/clientTemplates/addClientForm";
        }
        return "redirect:/clients";
    }

    @RequestMapping("/edit/{oldFirstName}/{oldLastName}")
    public String editClientForm(@PathVariable String oldFirstName, @PathVariable String oldLastName, Model model) {
        model.addAttribute("client", clientService.getClientByName(oldFirstName, oldLastName));
        model.addAttribute("oldFirstName", oldFirstName);
        model.addAttribute("oldLastName", oldLastName);
        return "/clientTemplates/editClientForm";
    }

    @PostMapping("/updateClient/{oldFirstName}/{oldLastName}")
    public String editClient(@PathVariable String oldFirstName, @PathVariable String oldLastName,
                          @ModelAttribute("client") @Valid PartialClientDto partialClientDto,
                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/clientTemplates/editClientForm";
        }
        try{
            clientService.updateClientInfo(oldFirstName, oldLastName, partialClientDto);
        }catch (Exception exception){
            bindingResult.reject("globalError", exception.getMessage());
            return "/clientTemplates/editClientForm";
        }
        return "redirect:/clients";
    }

    @RequestMapping("/delete/{firstName}/{lastName}")
    public String deleteClient(@PathVariable String firstName, @PathVariable String lastName){
        clientService.deleteClient(firstName, lastName);
        return "redirect:/clients";
    }
}
