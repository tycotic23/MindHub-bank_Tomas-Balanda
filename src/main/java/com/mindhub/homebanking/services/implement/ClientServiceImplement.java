package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return findAllClient().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public Client findClientById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> findAllClient() {
        return clientRepository.findAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public ClientDTO getClientDTOById(long id) {
        return new ClientDTO(findClientById(id));
    }

    @Override
    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    @Override
    public ClientDTO getClientDTOByEmail(String email) {
        return new ClientDTO(findClientByEmail(email));
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public boolean existsById(long id) {
        return clientRepository.existsById(id);
    }


}
