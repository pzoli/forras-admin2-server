package hu.infokristaly.keycloakauthenticatoin;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@SecurityScheme(
        name = "Keycloak"
       // , openIdConnectUrl = "http://lenovo-e16g2.me.local:8080/realms/infokristaly/.well-known/openid-configuration"
        , openIdConnectUrl = "https://exprog.hu:9443/realms/infokristaly/.well-known/openid-configuration"
        , scheme = "bearer"
        , type = SecuritySchemeType.OPENIDCONNECT
        , in = SecuritySchemeIn.HEADER
)
public class KeycloakAuthenticatoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycloakAuthenticatoinApplication.class, args);
    }

}
