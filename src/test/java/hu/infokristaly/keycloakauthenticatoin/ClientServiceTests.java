package hu.infokristaly.keycloakauthenticatoin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.repository.ClientRepository;
import hu.infokristaly.keycloakauthenticatoin.services.ClientService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(2)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@DisplayName("Test clients")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientServiceTests extends JWTBasedUnitTests {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private Client client;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()) // Biztonsági konfiguráció hozzáadása
                .build();
        getToken();
        client = new Client();
        client.setNeve("Teszt Elek");
        client.setNyilvantartasiSzam("123/123/123");
        client.setFelvetDatum(new Date());
        client.setTaj("123-123-123");
    }

    @DisplayName("Test client service")
    @Order(1)
    @Test
    public void testClientService() throws Exception {
        when(clientRepository.save(client)).thenReturn(new Client());
        clientService.createClient(client);
        verify(clientRepository).save(any(Client.class));
    }

    @DisplayName("Test client create adn delete")
    @Order(2)
    @Test
    public void testClientCreateDelete() throws Exception {
        when(clientRepository.existsById(client.getId())).thenReturn(true);

        MockHttpServletRequestBuilder request = post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.bearer)
                .content(new ObjectMapper().writeValueAsString(client));

        MvcResult result = this.mockMvc.perform(request
                ).andDo(print())
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonParser parser= mapper.createParser(result.getResponse().getContentAsByteArray());
        Client responseObj = parser.readValueAs(Client.class);
        assertNotNull(responseObj);

        MockHttpServletRequestBuilder deleteRequest = delete("/api/client/"+ responseObj.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.bearer)
                .content(new ObjectMapper().writeValueAsString(client));

        this.mockMvc.perform(deleteRequest
                ).andDo(print())
                .andExpect(status().isOk());
    }

}
