package io.github.gabrielgnoga.nexus_core_ledger.controller;

import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import io.github.gabrielgnoga.nexus_core_ledger.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TestController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})

class TestControllerTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @Test
    void deveRetornarMensagemDeAcessoLiberadoComStatus200() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/v1/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Acesso Liberado! Você está na área VIP da API e seu crachá é válido! "));
    }

    @Test
    void deveLancarIllegalArgumentExceptionNaRotaDeErro() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(get("/v1/test/erro"))

                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Testando o nosso para-quedas de erro global! O recurso não foi encontrado.", result.getResolvedException().getMessage()));
    }
}