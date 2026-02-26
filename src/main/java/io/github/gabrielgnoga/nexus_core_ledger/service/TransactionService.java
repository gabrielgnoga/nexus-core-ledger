package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionRequestDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import io.github.gabrielgnoga.nexus_core_ledger.repository.TransactionRepository;
import io.github.gabrielgnoga.nexus_core_ledger.exception.InsufficientBalanceException;
import io.github.gabrielgnoga.nexus_core_ledger.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio das transações financeiras.
 *
 * <p>Esta classe orquestra a comunicação entre os controladores e o banco de dados,
 * garantindo a integridade dos dados e a aplicação das regras financeiras
 * (como validação de saldo e consulta de extrato).</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    /**
     * Processa e registra uma nova transação financeira.
     *
     * @param data DTO contendo os dados da transação (conta, valor, tipo).
     * @return DTO com os dados da transação processada e salva.
     * @throws ResourceNotFoundException Se o ID da conta não for encontrado.
     * @throws InsufficientBalanceException Se a operação for um DÉBITO e o saldo for insuficiente.
     */
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
            account.setBalance(BigDecimal.ZERO);
        }

        if (transaction.getType() == TransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(data.amount()));
        } else {
            BigDecimal novoSaldo = account.getBalance().subtract(data.amount());

            if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException(
                        "Saldo insuficiente para realizar a operação. Saldo atual: " + account.getBalance()
                );
            }
            account.setBalance(novoSaldo);
        }

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return TransactionResponseDTO.fromEntity(transaction);
    }

    /**
     * Retorna o extrato completo de uma conta (Histórico de transações).
     *
     * @param accountId O ID da conta.
     * @return Lista de transações ordenadas da mais recente para a mais antiga.
     */
    public List<TransactionResponseDTO> getStatement(UUID accountId) {


        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Conta não encontrada com ID: " + accountId);
        }

        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream()
                .map(TransactionResponseDTO::fromEntity)
                .toList();
    }
}