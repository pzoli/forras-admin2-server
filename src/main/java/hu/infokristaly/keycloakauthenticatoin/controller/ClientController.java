package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.services.ClientService;
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
    ClientService clientService;

    public ClientController() {
        super();
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping(path="/{clientId}")
    @PreAuthorize("hasRole('user') or hasRole('manager')")
    public ResponseEntity<Client> getClient(@PathVariable(value = "clientId") Long clientId) {
        Client client = clientService.getClient(clientId);
        return client == null ? new ResponseEntity<Client>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(client);
    }

    @PostMapping
    @PreAuthorize("hasRole('manager')")
    public Client createClient(@RequestBody Client client) {
        client.setId(null);
        return clientService.createClient(client);
    }

    @PutMapping
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        Client origin = clientService.getClient(client.getId());
        if (origin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        client = clientService.updateClient(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        Client client = clientService.getClient(id);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            clientService.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
