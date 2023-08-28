package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientService {

    public ResponseEntity<Object> getClientDTO();

    public ResponseEntity<Object> getClientDTO(long id);

    public ResponseEntity<Object> getClientDTOByEmail(String email);

    public ResponseEntity<Object> saveClient(String firstName, String lastName, String email, String password);
}
