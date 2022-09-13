package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.models.Client;


import java.util.List;

public interface ClientService {
    public List<Client> getAllClients();
    public Client getClientById(Long id);
    public Client saveClient(Client client);
    public Client findByEmail(String email);
}
