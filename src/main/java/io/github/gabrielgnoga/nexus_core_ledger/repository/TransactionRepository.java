package io.github.gabrielgnoga.nexus_core_ledger.repository;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositório de dados para a entidade Transação (Transaction).
 *
 * <p>Utiliza o ecossistema do Spring Data JPA para fornecer operações de banco de dados
 * sem a necessidade de escrever queries SQL manualmente. Atua como a ponte de
 * comunicação direta entre a aplicação e a tabela 'transactions'.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Busca o extrato completo de uma conta, ordenado do mais recente para o mais antigo.
     *
     * <p>A query SQL (SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC)
     * é gerada automaticamente pelo Spring Data baseada no nome deste método (Query Method).</p>
     *
     * @param accountId O UUID único da conta bancária cujas transações serão listadas.
     * @return Uma lista contendo o histórico de transações da conta, ordenado de forma decrescente pela data.
     */
    List<Transaction> findByAccountIdOrderByTimestampDesc(UUID accountId);

}