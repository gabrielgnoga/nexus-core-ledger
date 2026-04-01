package io.github.gabrielgnoga.nexus_core_ledger.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@AutoConfigureMockMvc(addFilters = false)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

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