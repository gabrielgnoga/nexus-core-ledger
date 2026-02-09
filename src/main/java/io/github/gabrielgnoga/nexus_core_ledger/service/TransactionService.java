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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository; // Precisamos dele pra achar a conta!

    @Transactional // Garante que tudo acontece ou nada acontece (Atomicidade)
    public TransactionResponseDTO create(TransactionRequestDTO data) {
        // 1. Buscar a conta (Se não achar, grita erro)
        Account account = accountRepository.findById(data.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com ID: " + data.accountId()));

        // 2. Criar a Transação
        Transaction transaction = new Transaction();
        transaction.setAmount(data.amount());
        transaction.setType(data.type());
        transaction.setDescription(data.description());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account); // Aqui ligamos a transação à conta!

        // 3. Atualizar o Saldo da Conta (Regra de Negócio)
        // Se é CRÉDITO, soma. Se é DÉBITO, subtrai.
        if (transaction.getType() == TransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(data.amount()));
        } else {
            account.setBalance(account.getBalance().subtract(data.amount()));
        }

        // 4. Salvar as alterações
        accountRepository.save(account); // Salva o novo saldo
        transactionRepository.save(transaction); // Salva o histórico

        // 5. Retornar o DTO
        return TransactionResponseDTO.fromEntity(transaction);
    }
}