package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Objeto de Transferência de Dados (DTO) para respostas de Conta.
 *
 * <p>Versão imutável (Record) que garante a integridade dos dados
 * durante o transporte da API para o cliente.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Schema(description = "Resposta contendo os detalhes da conta bancária")
public record AccountResponseDTO(

        @Schema(description = "ID único da conta", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
        UUID id,

        @Schema(description = "Nome de identificação (Apelido)", example = "Minha Carteira Principal")
        String name,

        @Schema(description = "Saldo atual disponível", example = "1250.50")
        BigDecimal balance,

        @Schema(description = "Tipo da conta", example = "CHECKING")
        AccountType accountType,

        @Schema(description = "Data de criação da conta", example = "2026-02-11T10:00:00")
        LocalDateTime createdAt
) {

    /**
     * Converte uma Entidade {@link Account} para este DTO.
     * Método estático para facilitar a conversão no Controller.
     */
    public static AccountResponseDTO fromEntity(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getName(),
                account.getBalance(),
                account.getAccountType(),
                LocalDateTime.now()
        );
    }
}