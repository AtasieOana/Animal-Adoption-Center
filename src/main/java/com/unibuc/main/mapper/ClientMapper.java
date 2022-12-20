package com.unibuc.main.mapper;

import com.unibuc.main.dto.ClientDto;
import com.unibuc.main.entity.Client;
import com.unibuc.main.entity.PersonDetails;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client mapToClient(ClientDto clientDto) {
        return Client.builder()
                .personDetails(PersonDetails.builder().firstName(clientDto.getFirstName())
                        .lastName(clientDto.getLastName())
                        .phoneNumber(clientDto.getPhoneNumber()).build())
                .dateBecomingClient(clientDto.getDateBecomingClient())
                .birthDate(clientDto.getBirthDate())
                .gender(clientDto.getGender()).build();
    }

    public ClientDto mapToClientDto(Client client){
        return ClientDto.builder()
                .firstName(client.getPersonDetails().getFirstName())
                .lastName(client.getPersonDetails().getLastName())
                .dateBecomingClient(client.getDateBecomingClient())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .phoneNumber(client.getPersonDetails().getPhoneNumber()).build();
    }
}
