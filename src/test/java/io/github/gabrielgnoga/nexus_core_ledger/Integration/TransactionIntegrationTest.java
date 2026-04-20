package io.github.gabrielgnoga.nexus_core_ledger.Integration;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import io.github.gabrielgnoga.nexus_core_ledger.repository.TransactionRepository;
import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import io.github.gabrielgnoga.nexus_core_ledger.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
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
                "api.security.token.secret=minha-chave-secreta-de-teste-12345678"
        }
)
@ActiveProfiles("test")
class TransactionIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TokenService tokenService;

    private String tokenValido;
    private Account contaAlvo;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        User admin = new User("admin@nexus.com", "senha123");
        userRepository.save(admin);

        tokenValido = tokenService.generateToken(admin);

        contaAlvo = Account.builder()
                .name("Conta de Investimentos")
                .accountType(AccountType.valueOf("ASSET"))
                .currency("BRL")
                .balance(BigDecimal.ZERO)
                .build();
        accountRepository.save(contaAlvo);
    }

    @Test
    void deveProcessarDepositoComSucessoEAtualizarSaldoDaConta() {

        // ARRANGE
        String jsonRequest = """
                {
                    "accountId": "%s",
                    "amount": 500.00,
                    "type": "CREDIT",
                    "description": "Depósito inicial"
                }
                """.formatted(contaAlvo.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenValido);

        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        // ACT
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/transactions", request, String.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("🚨 ERRO NO SERVIDOR: " + response.getBody());
        }

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        List<Transaction> historico = transactionRepository.findAll();
        assertThat(historico).hasSize(1);
        assertThat(historico.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));

        Account contaAtualizada = accountRepository.findById(contaAlvo.getId()).orElseThrow();
        assertThat(contaAtualizada.getBalance()).isEqualByComparingTo(new BigDecimal("500.00"));
    }
}