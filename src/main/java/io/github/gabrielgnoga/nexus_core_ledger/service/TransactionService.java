package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionRequestDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import io.github.gabrielgnoga.nexus_core_ledger.repository.TransactionRepository;
import io.github.gabrielgnoga.nexus_core_ledger.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO data) {
        Account account = accountRepository.findById(data.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com ID: " + data.accountId()));


        Transaction transaction = new Transaction();
        transaction.setAmount(data.amount());
        transaction.setType(data.type());
        transaction.setDescription(data.description());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);}

        if (transaction.getType() == TransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(data.amount()));
        } else {
            account.setBalance(account.getBalance().subtract(data.amount()));
        }

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return TransactionResponseDTO.fromEntity(transaction);
    }
}