package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Objeto de Transferência de Dados (DTO) para respostas de Conta.
 *
 * <p>Esta classe define exatamente o que será devolvido no JSON para o cliente (Frontend/API).
 * Ela serve como uma camada de proteção, garantindo que apenas dados públicos sejam expostos,
 * ocultando detalhes internos da entidade {@code Account}.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Data
public class AccountResponseDTO {

    private UUID id;
    private String name;
    private BigDecimal balance;
    private AccountType accountType;
    private LocalDateTime createdAt;

    public AccountResponseDTO() {}

    /**
     * Construtor completo para facilitar a criação do DTO.
     *
     * @param id Identificador da conta.
     * @param name Nome da conta.
     * @param balance Saldo atual.
     * @param accountType Categoria contábil.
     * @param createdAt Data de criação.
     */
    public AccountResponseDTO(UUID id, String name, BigDecimal balance, AccountType accountType, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }
}
