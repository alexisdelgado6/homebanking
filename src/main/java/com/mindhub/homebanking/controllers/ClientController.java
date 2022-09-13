package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClients().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.getClientById(id));
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(!email.contains("@")){
            return new ResponseEntity<>("Enter a valid email.", HttpStatus.FORBIDDEN);
        }
        if(password.length()<5){
            return new ResponseEntity<>("The password is too short.", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email)!= null){
            return new ResponseEntity<>("Email already in use.", HttpStatus.FORBIDDEN);
        }
        String randomNumber = "VIN-"+getRandomNumber(00000000,99999999);
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        Account account = new Account(randomNumber, LocalDateTime.now(),0.0,client,AccountType.CURRENT);
        accountService.saveAccount(account);
        return new ResponseEntity<>("User registred.", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
