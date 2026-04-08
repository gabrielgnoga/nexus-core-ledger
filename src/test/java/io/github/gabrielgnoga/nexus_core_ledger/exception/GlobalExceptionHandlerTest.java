package io.github.gabrielgnoga.nexus_core_ledger.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    /**
     * Controller interno exclusivo para forçar o lançamento das exceções
     * e testar se o GlobalExceptionHandler as captura e formata corretamente.
     */
    @RestController
    class DummyController {
        @GetMapping("/teste-404")
        public void throwIllegalArgument() {
            throw new IllegalArgumentException("Conta não encontrada no banco de dados");
        }

        @GetMapping("/teste-422")
        public void throwInsufficientBalance() {
            throw new InsufficientBalanceException("Saldo insuficiente para concluir o saque");
        }

        @GetMapping("/teste-500")
        public void throwGenericException() throws Exception {
            throw new Exception("Falha repentina de conexão");
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveCapturarIllegalArgumentExceptionERetornarStatus404() throws Exception {
        mockMvc.perform(get("/teste-404"))
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.message").value("Conta não encontrada no banco de dados"))
                .andExpect(jsonPath("$.path").value("/teste-404"));
    }

    @Test
    void deveCapturarInsufficientBalanceExceptionERetornarStatus422() throws Exception {
        mockMvc.perform(get("/teste-422"))
                .andExpect(status().isUnprocessableEntity()) // 422
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Regra de Negócio / Saldo Insuficiente"))
                .andExpect(jsonPath("$.message").value("Saldo insuficiente para concluir o saque"))
                .andExpect(jsonPath("$.path").value("/teste-422"));
    }

    @Test
    void deveCapturarExceptionGenericaERetornarStatus500() throws Exception {
        mockMvc.perform(get("/teste-500"))
                .andExpect(status().isInternalServerError()) // 500
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Erro Interno do Servidor"))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro inesperado. Por favor, contate o suporte."))
                .andExpect(jsonPath("$.path").value("/teste-500"));
    }
}