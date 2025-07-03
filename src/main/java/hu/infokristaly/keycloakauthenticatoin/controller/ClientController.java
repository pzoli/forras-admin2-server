package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.repository.ClientRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@SecurityRequirement(name = "Keycloak")
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    public ClientController() {
        super();
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping(path="/{clientId}")
    @PreAuthorize("hasRole('user') or hasRole('manager')")
    public ResponseEntity<Client> getClient(@PathVariable(value = "clientId") Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        return client == null ? new ResponseEntity<Client>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(client);
    }

    @PostMapping
    @PreAuthorize("hasRole('manager')")
    public Client createClient(@RequestBody Client client) {
        client.setId(null);
        return clientRepository.save(client);
    }

    @PutMapping
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        Client origin = clientRepository.findById(client.getId()).orElse(null);
        if (origin == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        client = clientRepository.save(client);
        return new ResponseEntity(client, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        try {
            clientRepository.delete(client);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
