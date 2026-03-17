package io.github.gabrielgnoga.nexus_core_ledger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(

        @NotBlank
        @Email(message = "O formato deve ser um e-mail válido")
        String login,

        @NotBlank
        String password

) {}