package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientService {

    public List<ClientDTO> getClientDTO();

    public ClientDTO getClientDTO(long id);

    public ClientDTO getClientDTOByEmail(String email);

    public ResponseEntity<Object> saveClient(String firstName, String lastName, String email, String password);
}
