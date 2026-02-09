package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Transaction;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime timestamp,
        String description,
        UUID accountId
) {

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