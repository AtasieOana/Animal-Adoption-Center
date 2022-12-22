package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.dto.PartialClientDto;
import com.unibuc.main.entity.*;
import com.unibuc.main.entity.Client;
import com.unibuc.main.exception.ClientAlreadyExistsException;
import com.unibuc.main.exception.ClientNotFoundException;
import com.unibuc.main.mapper.ClientMapper;
import com.unibuc.main.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream().map(v -> clientMapper.mapToClientDto(v))
                .collect(Collectors.toList());
    }

    public ClientDto getClientByName(String firstName, String lastName) {
        Optional<Client> client = clientRepository.findClientByName(firstName, lastName);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(String.format(ProjectConstants.CLIENT_NOT_FOUND, firstName + ' ' + lastName));
        }
        return clientMapper.mapToClientDto(client.get());
    }
    public ClientDto addNewClient(ClientDto clientDto) {
        if(clientRepository.findClientByName(clientDto.getFirstName(), clientDto.getLastName()).isPresent()){
            throw new ClientAlreadyExistsException(String.format(ProjectConstants.CLIENT_EXISTS,clientDto.getFirstName() + " " + clientDto.getLastName()));
        }
        return clientMapper.mapToClientDto(clientRepository.save(clientMapper.mapToClient(clientDto)));
    }

    public void deleteClient(String firstName, String lastName) {
        Optional<Client> client = clientRepository.findClientByName(firstName, lastName);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(String.format(ProjectConstants.CLIENT_NOT_FOUND, firstName + ' ' + lastName));
        }
        clientRepository.delete(client.get());
    }

    public Integer getAvgAgeForClient() {
        Double avgAge = clientRepository.avgAge();
        if(avgAge != null){
            return avgAge.intValue();
        }
        else{
            return 0;
        }
    }

    public String getClientNumberByGender() {
        Integer totalClients = clientRepository.totalClients();
        Integer totalClientsMale = clientRepository.totalClientsMale();
        Integer totalClientsFemale = totalClients - totalClientsMale;
        return String.format(ProjectConstants.CLIENT_GENDERS, totalClients, totalClientsFemale, totalClientsMale);
    }

    public ClientDto updateClientInfo(String oldFirstName, String oldLastName, PartialClientDto newClientInfo) {
        Optional<Client> client = clientRepository.findClientByName(oldFirstName, oldLastName);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(String.format(ProjectConstants.CLIENT_NOT_FOUND, oldFirstName + ' ' + oldLastName));
        }
        Client updateClient = client.get();
        PersonDetails pd = updateClient.getPersonDetails();
        pd.setFirstName(newClientInfo.getFirstName() != null ? newClientInfo.getFirstName() : oldFirstName);
        pd.setLastName(newClientInfo.getLastName() != null ? newClientInfo.getLastName() : oldLastName);
        pd.setPhoneNumber(newClientInfo.getPhoneNumber() != null ? newClientInfo.getPhoneNumber() : updateClient.getPersonDetails().getPhoneNumber());
        return clientMapper.mapToClientDto(clientRepository.save(updateClient));
    }


}
