package io.github.gabrielgnoga.nexus_core_ledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountDTO {

    // O nome da conta (ex: "Carteira Principal", "Reserva de Emergência")
    @NotBlank(message = "O nome da conta é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;

    // O tipo da conta (ex: CORRENTE, POUPANCA, INVESTIMENTO)
    // Aqui assumo que o usuário escolhe o tipo ao criar.
    @NotNull(message = "O tipo da conta é obrigatório")
    private String accountType;

    // Nota: Se AccountType for um Enum no seu sistema,
    // você pode usar o próprio Enum aqui em vez de String.

    // ATENÇÃO:
    // 1. Sem 'balance': Como combinamos, toda conta nasce com 0.0000.
    // 2. Sem 'id': O banco gera o UUID.
    // 3. Sem 'createdAt': O @PrePersist resolve.
}