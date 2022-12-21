package com.unibuc.main.controller;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    
    @GetMapping()
    public ResponseEntity<List<ClientDto>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }
    
    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<ClientDto> getClientByName(@PathVariable String firstName, @PathVariable String lastName){
        return ResponseEntity.ok(clientService.getClientByName(firstName, lastName));
    }

    @GetMapping("/avgAgeForClient")
    public ResponseEntity<Integer> getAvgAgeForClient(){
        return ResponseEntity.ok(clientService.getAvgAgeForClient());
    }

    @GetMapping("/clientsNumberByGender")
    public ResponseEntity<String> getClientNumberByGender(){
        return ResponseEntity.ok(clientService.getClientNumberByGender());
    }

    @PostMapping
    public ResponseEntity<ClientDto> addNewClient(@Valid @RequestBody ClientDto clientDto){
        return ResponseEntity.ok(clientService.addNewClient(clientDto));
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteClient(@PathVariable String firstName, @PathVariable String lastName){
        clientService.deleteClient(firstName, lastName);
        return ResponseEntity.ok(ProjectConstants.OBJ_DELETED);
    }

    @PutMapping("/{oldFirstName}/{oldLastName}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable String oldFirstName, @PathVariable String oldLastName, @Valid @RequestBody PartialClientDto newInfoClient){
        return ResponseEntity.ok(clientService.updateClientInfo(oldFirstName, oldLastName, newInfoClient));
    }



}
