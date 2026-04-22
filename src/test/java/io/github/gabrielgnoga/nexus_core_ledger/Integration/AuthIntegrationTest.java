package io.github.gabrielgnoga.nexus_core_ledger.Integration;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:nexus_test_db",
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "api.security.token.secret=minha-chave-secreta-de-teste-12345678"
        }
)
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() {

        // ARRANGE
        String jsonRegister = """
                {
                    "login": "novo.cliente@nexus.com",
                    "password": "senhaSegura123"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonRegister, headers);

        // ACT
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/auth/register", request, String.class);

        if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("ERRO NO REGISTRO: " + response.getBody());
        }

        // ASSERT
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.CREATED);

        assertThat(userRepository.findAll()).hasSize(1);

        User usuarioSalvo = userRepository.findAll().get(0);
        assertThat(usuarioSalvo.getLogin()).isEqualTo("novo.cliente@nexus.com");
        assertThat(usuarioSalvo.getPassword()).isNotEqualTo("senhaSegura123");
    }

    @Test
    void deveFazerLoginERetornarTokenJwt() {

        // ARRANGE
        User usuarioExistente = new User("investidor@nexus.com", passwordEncoder.encode("senha123"));
        userRepository.save(usuarioExistente);

        String jsonLogin = """
                {
                    "login": "investidor@nexus.com",
                    "password": "senha123"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonLogin, headers);

        // ACT
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/auth/login", request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println(" ERRO NO LOGIN: " + response.getBody());
        }

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("token");
    }
}