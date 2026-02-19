package io.github.gabrielgnoga.nexus_core_ledger.dto;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) responsável por capturar os dados iniciais
 * para a criação de uma nova conta.
 *
 * <p>Esta classe garante que os dados recebidos da API atendam aos requisitos mínimos
 * (validação) antes de serem processados pela camada de serviço.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Dados para abertura de nova conta")
public class CreateAccountDTO {

    @Schema(description = "Nome da conta (Apelido)", example = "Minha Carteira")
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 50)
    private String name;



    @Schema(description = "Tipo da conta")
    @NotNull(message = "O tipo da conta é obrigatório")
    private AccountType accountType;

}