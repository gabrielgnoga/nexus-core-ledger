package io.github.gabrielgnoga.nexus_core_ledger.repository;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void deveBuscarTransacoesPorContaEOrdenarPorDataDecrescente() {

// ARRANGE
        Account contaAlvo = Account.builder()
                .name("Conta Corrente Principal")
                .accountType(AccountType.EQUITY)
                .build();
        contaAlvo = entityManager.persistAndFlush(contaAlvo);

        Account contaDeOutraPessoa = Account.builder()
                .name("Conta Poupança de Terceiro")
                .accountType(AccountType.LIABILITY)
                .build();
        contaDeOutraPessoa = entityManager.persistAndFlush(contaDeOutraPessoa);

//creating transactions using setters
        Transaction transacaoAntiga = new Transaction();
        transacaoAntiga.setAmount(BigDecimal.valueOf(100));
        transacaoAntiga.setType(TransactionType.CREDIT);
        transacaoAntiga.setTimestamp(LocalDateTime.now().minusDays(2));//setting a past date to force correct ordenation
        transacaoAntiga.setDescription("Depósito Antigo");
        transacaoAntiga.setAccount(contaAlvo);

        Transaction transacaoNova = new Transaction();
        transacaoNova.setAmount(BigDecimal.valueOf(50));
        transacaoNova.setType(TransactionType.DEBIT);
        transacaoNova.setTimestamp(LocalDateTime.now());
        transacaoNova.setDescription("Compra Recente");
        transacaoNova.setAccount(contaAlvo);

        Transaction transacaoInvasora = new Transaction();
        transacaoInvasora.setAmount(BigDecimal.valueOf(200));
        transacaoInvasora.setType(TransactionType.CREDIT);
        transacaoInvasora.setTimestamp(LocalDateTime.now());
        transacaoInvasora.setDescription("Conta Errada");
        transacaoInvasora.setAccount(contaDeOutraPessoa);

//save transactions
        entityManager.persist(transacaoAntiga);
        entityManager.persist(transacaoNova);
        entityManager.persist(transacaoInvasora);
        entityManager.flush();


        // ACT
        List<Transaction> resultado = transactionRepository.findByAccountIdOrderByTimestampDesc(contaAlvo.getId());

        // ASSERT
        assertThat(resultado).hasSize(2);

        assertThat(resultado.get(0).getDescription()).isEqualTo("Compra Recente");
        assertThat(resultado.get(1).getDescription()).isEqualTo("Depósito Antigo");
    }
}