package hu.infokristaly.keycloakauthenticatoin;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public class JWTBasedUnitTests {
    @Value("${KEYCLOAK_CLIENT_ID}")
    private String client_id;

    @Value("${KEYCLOAK_TEST_USER}")
    private String user;

    @Value("${KEYCLOAK_TEST_PASSWORD}")
    private String password;

    protected MockMvc mockMvc;
    protected String bearer;

    private String getBearer(String result) {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return "Bearer " + jsonParser.parseMap(result)
                .get("access_token")
                .toString();
    }

    protected void getToken() throws URISyntaxException {
        this.bearer = getBearer(getJWTFromKeycloakServer());
    }

    private String getJWTFromKeycloakServer() throws URISyntaxException {
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
