package hu.infokristaly.keycloakauthenticatoin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.infokristaly.keycloakauthenticatoin.entity.Client;
import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import hu.infokristaly.keycloakauthenticatoin.repository.ClientRepository;
import hu.infokristaly.keycloakauthenticatoin.services.ClientService;
import hu.infokristaly.keycloakauthenticatoin.services.DoctorService;
import jakarta.servlet.ServletContext;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
class KeycloakAuthenticatoinApplicationTests {

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String client_id;

    @Value("${KEYCLOAK_TEST_USER}")
    private String user;

    @Value("${KEYCLOAK_TEST_PASSWORD}")
    private String password;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorService doctorService;

    @Mock
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private MockMvc mockMvc;
    private String bearer;

    private Client client;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()) // Biztonsági konfiguráció hozzáadása
                .build();
        this.bearer = getToken(getBearerFromKeycloakServer());

        client = new Client();
        client.setNeve("Teszt Elek");
        client.setNyilvantartasiSzam("123/123/123");
        client.setFelvetDatum(new Date());
        client.setTaj("123-123-123");
    }

    @Test
    void contextLoads() {

    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("doctorController"));
    }

    @Test
    public void testDoctorService() throws Exception {
        this.mockMvc.perform(get("/api/doctor")
                        .header("Authorization", this.bearer)
                ).andDo(print())
                .andExpect(status().isOk());
        Doctor doctor = doctorService.getDoctor(4L);
        assertNotNull(doctor);
    }

    @Test
    public void testClientService() throws Exception {
        clientService.createClient(client);
        when(clientRepository.save(client)).thenReturn(new Client());
        verify(clientService).createClient(any(Client.class));
    }

    @Test
    public void testClientCreateDelete() throws Exception {
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

    private String getToken(String result) {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return "Bearer " + jsonParser.parseMap(result)
                .get("access_token")
                .toString();
    }

    private String getBearerFromKeycloakServer() throws URISyntaxException {
        URI authorizationURI = new URIBuilder("https://exprog.hu:9443/realms/infokristaly/protocol/openid-connect/token").build();
        WebClient webclient = WebClient.builder().build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList(client_id));
        formData.put("username", Collections.singletonList(user));
        formData.put("password", Collections.singletonList(password));

        return webclient.post()
                .uri(authorizationURI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

}
