package io.github.gabrielgnoga.nexus_core_ledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
@Data
public class CreateAccountDTO {

    @NotBlank(message = "O nome da conta é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;

    @NotNull(message = "O tipo da conta é obrigatório")
    private String accountType;

}