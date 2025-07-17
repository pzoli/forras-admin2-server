package hu.infokristaly.keycloakauthenticatoin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.infokristaly.keycloakauthenticatoin.controller.ClientController;
import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.exceptions.ClientNotFoundException;
import hu.infokristaly.keycloakauthenticatoin.services.ClientService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class ClientControllerTests extends JWTBasedUnitTests {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client client;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
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
    public void testClientController() throws Exception {
        when(clientService.createClient(any(Client.class))).thenReturn(client);

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

        verify(clientService).createClient(any(Client.class));
    }

    @Test
    @Order(2)
    public void testClientController_ClientNotFoundException() throws Exception {
        when(clientService.getClient(any())).thenThrow(new ClientNotFoundException("Client not found"));

        MockHttpServletRequestBuilder request = put("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.bearer)
                .content(new ObjectMapper().writeValueAsString(client));

        MvcResult result = this.mockMvc.perform(request
                ).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value())).andReturn();

    }
}
