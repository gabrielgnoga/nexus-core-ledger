package io.github.gabrielgnoga.nexus_core_ledger.Integration;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import io.github.gabrielgnoga.nexus_core_ledger.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
                "spring.jpa.show-sql=false",
                "api.security.token.secret=minha-chave-secreta-de-teste-12345678"
        }
)
@ActiveProfiles("test")
class AccountIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private String tokenValido;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        User admin = new User("admin@nexus.com", "senha123");
        userRepository.save(admin);

        tokenValido = tokenService.generateToken(admin);
    }

    @Test
    void deveCriarContaComSucessoEGravarFisicamenteNoBanco() {

        // ARRANGE
        String jsonRequest = """
                {
                    "name": "Conta Corrente Teste",
                    "accountType": "EQUITY"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(tokenValido);

        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        // ACT
        ResponseEntity<String> response = restTemplate.postForEntity("/accounts", request, String.class);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        List<Account> contasNoBanco = accountRepository.findAll();
        assertThat(contasNoBanco).hasSize(1);
        assertThat(contasNoBanco.get(0).getName()).isEqualTo("Conta Corrente Teste");
        assertThat(contasNoBanco.get(0).getBalance()).isNotNull();
    }
}