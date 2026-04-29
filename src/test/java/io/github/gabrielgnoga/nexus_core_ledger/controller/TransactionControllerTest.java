package io.github.gabrielgnoga.nexus_core_ledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionRequestDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import io.github.gabrielgnoga.nexus_core_ledger.service.TokenService;
import io.github.gabrielgnoga.nexus_core_ledger.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TransactionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})

class TransactionControllerTest {

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    void deveCriarTransacaoComSucessoERetornarStatus201() throws Exception {

        // ARRANGE
        UUID accountId = UUID.randomUUID();

        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                accountId,
                BigDecimal.valueOf(150.00),
                TransactionType.CREDIT,
                "Depósito inicial"
        );
        TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                UUID.randomUUID(),
                BigDecimal.valueOf(150.00),
                TransactionType.CREDIT,
                LocalDateTime.now(),
                "Depósito inicial",
                accountId
        );

        when(transactionService.create(any(TransactionRequestDTO.class))).thenReturn(responseDTO);

        String jsonPayload = objectMapper.writeValueAsString(requestDTO);

        // ACT & ASSERT
        mockMvc.perform(post("/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(150.00));
    }

    @Test
    void deveRetornarExtratoDaContaComStatus200() throws Exception {

        // ARRANGE
        UUID accountId = UUID.randomUUID();

        TransactionResponseDTO transacao1 = new TransactionResponseDTO(UUID.randomUUID(), BigDecimal.valueOf(100.00), TransactionType.CREDIT, LocalDateTime.now(), "Transferência recebida", accountId);
        TransactionResponseDTO transacao2 = new TransactionResponseDTO(UUID.randomUUID(), BigDecimal.valueOf(50.00), TransactionType.DEBIT, LocalDateTime.now(), "Compra no débito", accountId);

        when(transactionService.getStatement(accountId)).thenReturn(List.of(transacao1, transacao2));

        // ACT & ASSERT
        mockMvc.perform(get("/v1/transactions/account/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}