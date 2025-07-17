package hu.infokristaly.keycloakauthenticatoin;

import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.AssertionErrors;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientEntityTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @Order(1)
    void TestClientEntity_whenValidClientEntity_thenSuccess() {
        Client client = new Client();
        client.setNeve("Teszt Elek");
        client.setTaj("123-123-123");
        client.setNyilvantartasiSzam("123/123/123");
        client.setFelvetDatum(new Date());
        Client storedClientEntity = testEntityManager.persistAndFlush(client);

        assertNotNull(storedClientEntity.getId());
    }
}
