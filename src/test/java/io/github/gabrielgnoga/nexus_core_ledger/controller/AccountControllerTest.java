package io.github.gabrielgnoga.nexus_core_ledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AccountResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void deveCriarContaERetornarStatus201Created() throws Exception {
        // ARRANGE
        CreateAccountDTO requestDTO = new CreateAccountDTO("Minha Carteira", AccountType.EQUITY);
        AccountResponseDTO responseDTO = new AccountResponseDTO(UUID.randomUUID(), "Conta Corrente", null, null, null);

        when(accountService.createAccount(any(CreateAccountDTO.class))).thenReturn(responseDTO);

        String jsonPayload = objectMapper.writeValueAsString(requestDTO);

        // ACT & ASSERT
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))


                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Conta Corrente"));
    }

    @Test
    void deveListarTodasAsContasERetornarStatus200Ok() throws Exception {
        // ARRANGE
        AccountResponseDTO conta1 = new AccountResponseDTO(UUID.randomUUID(), "Conta 1", null, null, null);
        AccountResponseDTO conta2 = new AccountResponseDTO(UUID.randomUUID(), "Conta 2", null, null, null);

        when(accountService.listAllAccounts()).thenReturn(List.of(conta1, conta2));

        // ACT & ASSERT

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Conta 1"));
    }
}