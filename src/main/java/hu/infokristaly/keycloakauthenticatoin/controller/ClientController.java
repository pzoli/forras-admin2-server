package hu.infokristaly.keycloakauthenticatoin.controller;

import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.repository.ClientReposigory;
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
    ClientReposigory clientReposigory;

    public ClientController() {
        super();
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public List<Client> getAllClients() {
        return clientReposigory.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('manager')")
    public Client createClient(@RequestBody Client client) {
        client.setId(null);
        return clientReposigory.save(client);
    }

    @PutMapping
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        Client origin = clientReposigory.findById(client.getId()).orElse(null);
        if (origin == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        client = clientReposigory.save(client);
        return new ResponseEntity(client, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        Client client = clientReposigory.findById(id).orElse(null);
        if (client == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        try {
            clientReposigory.delete(client);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FAILED_DEPENDENCY);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
