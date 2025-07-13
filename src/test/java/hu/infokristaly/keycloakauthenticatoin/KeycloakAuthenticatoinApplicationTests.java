package hu.infokristaly.keycloakauthenticatoin;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Order(1)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@DisplayName("Test over all")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KeycloakAuthenticatoinApplicationTests extends JWTBasedUnitTests {

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeAll
    public void setup() throws Exception {
    }

    @DisplayName("context load test")
    @Order(1)
    @Test
    void contextLoads() {

    }

    @DisplayName("Test servlet context by autowired webApplicationContext")
    @Order(2)
    @Test
    public void givenWac_whenServletContext_thenItProvidesDoctorController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("doctorController"));
    }

}
