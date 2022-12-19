package com.unibuc.main.mapper;

import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.entity.Client;
import com.unibuc.main.entity.EmployeeDetails;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client mapToClient(ClientDto clientDto) {
        return Client.builder()
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .dateBecomingClient(clientDto.getDateBecomingClient())
                .birthDate(clientDto.getBirthDate())
                .gender(clientDto.getGender())
                .phoneNumber(clientDto.getPhoneNumber()).build();
    }

    public ClientDto mapToClientDto(Client client){
        return ClientDto.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .dateBecomingClient(client.getDateBecomingClient())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .phoneNumber(client.getPhoneNumber()).build();
    }
}
