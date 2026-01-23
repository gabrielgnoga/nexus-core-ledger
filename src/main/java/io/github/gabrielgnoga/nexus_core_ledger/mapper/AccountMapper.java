package io.github.gabrielgnoga.nexus_core_ledger.mapper;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AccountResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela conversão entre Entidades e DTOs.
 *
 * <p>O Mapper centraliza a lógica de transformação de dados, evitando que essa responsabilidade
 * fique espalhada pelo Service ou Controller. Ele atua como um tradutor entre o Banco de Dados
 * (Entidade) e a API Pública (DTO).</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Component
public class AccountMapper {
    /**
     * Converte uma Entidade {@link Account} para um {@link AccountResponseDTO}.
     *
     * @param account A entidade original vinda do banco de dados.
     * @return O DTO formatado para resposta JSON, ou {@code null} se a entrada for nula.
     */
    public AccountResponseDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountResponseDTO(
                account.getId(),
                account.getName(),
                account.getBalance(),
                account.getAccountType(),
                account.getCreatedAt()
        );
    }
}