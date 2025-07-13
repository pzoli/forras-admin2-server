package hu.infokristaly.keycloakauthenticatoin;

import hu.infokristaly.keycloakauthenticatoin.entity.Doctor;
import hu.infokristaly.keycloakauthenticatoin.services.DoctorService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(1)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@DisplayName("Test doctor")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorTests extends JWTBasedUnitTests {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()) // Biztonsági konfiguráció hozzáadása
                .build();

        getToken();
    }

    @DisplayName("Test doctor service")
    @Order(3)
    @Test
    public void testDoctorService() throws Exception {
        this.mockMvc.perform(get("/api/doctor")
                        .header("Authorization", this.bearer)
                ).andDo(print())
                .andExpect(status().isOk());
        Doctor doctor = doctorService.getDoctor(4L);
        assertNotNull(doctor);
    }

}
