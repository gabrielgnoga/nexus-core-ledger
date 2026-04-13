package io.github.gabrielgnoga.nexus_core_ledger.repository;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void deveRetornarTrueQuandoNomeDaContaJaExistir() {

        // ARRANGE
        String nomeAlvo = "Conta Poupança Principal";

        Account conta = Account.builder()
                .name(nomeAlvo)
                .accountType(AccountType.ASSET)
                .build();

        entityManager.persistAndFlush(conta);

        // ACT
        boolean resultado = accountRepository.existsByName(nomeAlvo);

        // ASSERT
        assertThat(resultado).isTrue();
    }

    @Test
    void deveRetornarFalseQuandoNomeDaContaNaoExistir() {

        // ARRANGE
        String nomeInexistente = "Conta Fantasma";

        // ACT
        boolean resultado = accountRepository.existsByName(nomeInexistente);

        // ASSERT
        assertThat(resultado).isFalse();
    }
}