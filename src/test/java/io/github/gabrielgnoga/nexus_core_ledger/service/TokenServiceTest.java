package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "minha-chave-secreta-super-segura");
    }

    @Test
    void deveGerarTokenComSucesso() {
        // ARRANGE
        User user = new User("gabriel.dev", "senha123");

        // ACT
        String token = tokenService.generateToken(user);

        // ASSERT
        assertNotNull(token);
        assertFalse(token.isBlank());

        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void deveValidarTokenExtraindoLoginComSucesso() {

        // ARRANGE
        User user = new User("gabriel.dev", "senha123");

        String tokenValido = tokenService.generateToken(user);

        // ACT
        String subject = tokenService.validateToken(tokenValido);

        // ASSERT
        assertEquals("gabriel.dev", subject);
    }

    @Test
    void deveRetornarStringVaziaQuandoTokenForInvalido() {
        // ARRANGE
        String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.token.falso";

        // ACT
        String subject = tokenService.validateToken(tokenInvalido);

        // ASSERT
        assertEquals("", subject);
    }
}