package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.entity.Client;
import com.unibuc.main.service.ClientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    
    @GetMapping()
    @ApiOperation("Getting all center clients")
    public ResponseEntity<List<ClientDto>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }
    
    @GetMapping("/{firstName}/{lastName}")
    @ApiOperation("Getting a client by first and last name")
    public ResponseEntity<ClientDto> getClientByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(clientService.getClientByName(firstName, lastName));
    }

    @GetMapping("/avgAgeForClient")
    @ApiOperation("Calculating the average age for the clients")
    public ResponseEntity<Integer> getAvgAgeForClient(){
        return ResponseEntity.ok(clientService.getAvgAgeForClient());
    }

    @GetMapping("/clientsNumberByGender")
    @ApiOperation("Calculating the number of women and male clients")
    public ResponseEntity<String> getClientNumberByGender(){
        return ResponseEntity.ok(clientService.getClientNumberByGender());
    }

    @PostMapping
    @ApiOperation("Adding a new client to the system")
    public ResponseEntity<ClientDto> addNewClient(@Valid @RequestBody ClientDto clientDto){
        return ResponseEntity.ok(clientService.addNewClient(clientDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    @ApiOperation("Delete a client with a given first and last name")
    public ResponseEntity<String> deleteClient(@PathVariable String firstName, @PathVariable String lastName){
        clientService.deleteClient(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    @ApiOperation("Update the info (first name, last name, phone number) of a client")
    public ResponseEntity<ClientDto> updateClient(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody PartialClientDto newInfoClient){
        return ResponseEntity.ok(clientService.updateClientInfo(oldFirstName, oldLastName, newInfoClient));
    }
    @GetMapping("/getAllClients")
    public ModelAndView products(){
        ModelAndView modelAndView = new ModelAndView("clientList");
        List<ClientDto> clientList = clientService.getAllClients();
        modelAndView.addObject("clients",clientList);
        return modelAndView;
    }


}
