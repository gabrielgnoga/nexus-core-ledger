package io.github.gabrielgnoga.nexus_core_ledger.mapper;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AccountResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapper();
    }

    @Test
    void deveConverterEntidadeParaDTOComSucesso() {
        // ARRANGE
        Account contaOriginal = new Account();
        contaOriginal.setId(UUID.randomUUID());
        contaOriginal.setName("Conta Corrente do Gabriel");

         contaOriginal.setBalance(BigDecimal.ZERO);
         contaOriginal.setAccountType(AccountType.EQUITY);
         contaOriginal.setCreatedAt(LocalDateTime.now());

        // ACT
        AccountResponseDTO resultadoDTO = accountMapper.toDTO(contaOriginal);

        // ASSERT
        assertNotNull(resultadoDTO);

        assertEquals(contaOriginal.getId(), resultadoDTO.id());
        assertEquals(contaOriginal.getName(), resultadoDTO.name());
    }

    @Test
    void deveRetornarNullQuandoEntidadeForNula() {
        // ACT
        AccountResponseDTO resultadoDTO = accountMapper.toDTO(null);

        // ASSERT
        assertNull(resultadoDTO);
    }
}