package hu.infokristaly.keycloakauthenticatoin;

import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import hu.infokristaly.keycloakauthenticatoin.services.DoctorService;
import jakarta.servlet.ServletContext;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

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

    private MockMvc mockMvc;
    private String bearer;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()) // Biztonsági konfiguráció hozzáadása
                .build();
        this.bearer = getToken(getBearerFromKeycloakServer());
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
