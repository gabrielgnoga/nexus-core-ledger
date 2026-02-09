package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(

        @NotNull(message = "O ID da conta é obrigatório")
        UUID accountId, // O ID de quem vai receber/pagar

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount,

        @NotNull(message = "O tipo (CREDIT ou DEBIT) é obrigatório")
        TransactionType type,

        String description
) {}