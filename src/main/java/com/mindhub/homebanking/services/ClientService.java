package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    ClientDTO getClientDTOById(long id);
    ClientDTO getClientDTOByEmail(String email);

    List<ClientDTO> getAllClientDTO();

    Client findClientById(long id);
    Client findClientByEmail(String email);
    List<Client> findAllClient();

    boolean existsByEmail(String email);
    void saveClient(Client client);

    boolean existsById(long id);
}
