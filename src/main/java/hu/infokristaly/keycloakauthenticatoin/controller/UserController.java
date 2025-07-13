package hu.infokristaly.keycloakauthenticatoin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.admin.client.Keycloak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "Keycloak")
public class UserController {
    @Value("${keycloak.admin-cli.user}")
    private String cliUser;

    @Value("${keycloak.admin-cli.password}")
    private String cliPassword;

    @Value("${keycloak.admin-cli.server}")
    private String cliServerUrl;

    @GetMapping(path = "/info")
    public HashMap index() {

        //OAuth2IntrospectionAuthenticatedPrincipal user = (OAuth2IntrospectionAuthenticatedPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new HashMap() {{
            put("sub", user.getClaimAsString("sub"));
            put("name", user.getClaimAsString("name"));
            put("email", user.getClaimAsString("email"));
            put("roles", user.getClaimAsStringList("roles"));
            put("expiresAt", user.getExpiresAt());
        }};
    }

    @PutMapping(path = "/setlang/{lang}")
    public void updateLanguage(@PathVariable String lang) {
        Jwt userJWT = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Keycloak keycloak = Keycloak.getInstance(
                cliServerUrl,
                "master",
                cliUser,
                cliPassword,
                "admin-cli");

        UsersResource userResource = keycloak.realm("infokristaly").users();
        UserRepresentation user = userResource.get(userJWT.getClaimAsString("sub")).toRepresentation();

        if (user != null) {
            Map<String, List<String>> attribs = user.getAttributes();
            if (attribs.containsKey("language")) {
                attribs.get("language").set(0, lang);
                try {
                    userResource.get(user.getId()).update(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
