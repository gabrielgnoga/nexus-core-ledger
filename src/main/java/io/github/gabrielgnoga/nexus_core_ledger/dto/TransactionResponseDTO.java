package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
/**
 * DTO responsável por expor os dados de uma transação processada.
 *
 * <p>Utilizado para retornar informações seguras para o cliente,
 * ocultando detalhes complexos da entidade original (como mapeamentos do Hibernate).</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */

@Schema(description = "Resposta contendo os detalhes da transação processada")
public record TransactionResponseDTO(

        @Schema(description = "ID único da transação gerada", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Valor movimentado", example = "150.50")
        BigDecimal amount,

        @Schema(description = "Tipo da operação", example = "CREDIT")
        TransactionType type,

        @Schema(description = "Data e hora do processamento", example = "2026-02-11T14:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Descrição da transação", example = "Pagamento de Freelance")
        String description,

        @Schema(description = "ID da conta vinculada", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
        UUID accountId
) {
    /**
     * Converte uma entidade de domínio {@link Transaction} para este DTO.
     *
     * @param transaction A entidade persistida no banco.
     * @return Uma nova instância de TransactionResponseDTO.
     */
    public static TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getTimestamp(),
                transaction.getDescription(),
                transaction.getAccount().getId()
        );
    }
}