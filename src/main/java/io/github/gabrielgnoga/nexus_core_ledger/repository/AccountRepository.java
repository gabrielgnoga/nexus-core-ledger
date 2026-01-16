package io.github.gabrielgnoga.nexus_core_ledger.repository;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface de Repositório para a entidade {@link Account}.
 *
 * <p>Estende o {@link JpaRepository} para fornecer operações CRUD padrão
 * e abstrair a complexidade do SQL nativo. O Spring Data JPA implementa
 * esta interface automaticamente em tempo de execução.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Verifica se já existe uma conta cadastrada com o nome fornecido.
     *
     * <p>Utiliza uma consulta derivada (Derived Query) otimizada que retorna apenas um booleano,
     * evitando o overhead de carregar a entidade completa da memória (o que aconteceria com findByName).</p>
     *
     * @param name O nome da conta a ser verificado.
     * @return {@code true} se o nome já estiver em uso, {@code false} caso contrário.
     */
    boolean existsByName(String name);
}