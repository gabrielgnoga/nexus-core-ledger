package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;
/**
 * Objeto de Transferência de Dados (DTO) para solicitação de novas transações.
 *
 * <p>Recebe os dados brutos da API, valida a existência de campos obrigatórios
 * e regras básicas (como valores positivos) antes de passar para o Service.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Schema(description = "Requisição para criar uma nova transação financeira")
public record TransactionRequestDTO(

/**
 * O ID único da conta que sofrerá a alteração de saldo.
 * Deve corresponder a uma conta existente no banco de dados.
 */
        @Schema(description = "ID da conta de origem/destino", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
        @NotNull(message = "O ID da conta é obrigatório")
        UUID accountId,

/**
 * O valor monetário da transação.
 * Deve ser sempre positivo, independentemente se é crédito ou débito.
 */
        @Schema(description = "Valor da movimentação", example = "150.50")
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount,

/**
 * Define a direção da operação financeira (Entrada/Saída).
 */
        @Schema(description = "Tipo da operação: CREDIT (entrada) ou DEBIT (saída)", example = "CREDIT")
        @NotNull(message = "O tipo é obrigatório")
        TransactionType type,

/**
 * Uma breve descrição para identificação futura no extrato.
 * Campo opcional.
 */
        @Schema(description = "Descrição opcional", example = "Pagamento de Freelance")
        String description
) {}