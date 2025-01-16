package hu.infokristaly.keycloakauthenticatoin.repository;

import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientReposigory extends JpaRepository<Client, Long> {
}
