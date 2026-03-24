package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionRequestDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.exception.InsufficientBalanceException;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import io.github.gabrielgnoga.nexus_core_ledger.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void deveAdicionarSaldoComSucessoQuandoTransacaoForCredito() {
        // ARRANGE
        UUID accountId = UUID.randomUUID();
        TransactionRequestDTO request = new TransactionRequestDTO(
                accountId, new BigDecimal("100.00"), TransactionType.CREDIT, "Depósito inicial"
        );

        Account contaFalsa = new Account();
        contaFalsa.setId(accountId);
        contaFalsa.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(contaFalsa));
        // ACT

        TransactionResponseDTO response = transactionService.create(request);

        // ASSERT
        assertNotNull(response);
        assertEquals(new BigDecimal("150.00"), contaFalsa.getBalance());

        verify(accountRepository, times(1)).save(contaFalsa);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverSaldoNoDebito() {
        // ARRANGE

        UUID accountId = UUID.randomUUID();
        TransactionRequestDTO request = new TransactionRequestDTO(
                accountId, new BigDecimal("200.00"), TransactionType.DEBIT, "Compra cara"
        );

        Account contaFalsa = new Account();
        contaFalsa.setId(accountId);
        contaFalsa.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(contaFalsa));

        // ACT & ASSERT
        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.create(request);
        });

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}