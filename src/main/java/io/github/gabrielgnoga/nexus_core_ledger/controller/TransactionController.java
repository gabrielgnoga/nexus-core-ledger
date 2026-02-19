package io.github.gabrielgnoga.nexus_core_ledger.controller;

import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionRequestDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.TransactionResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável por expor os endpoints de transações financeiras.
 *
 * <p>Recebe as requisições HTTP, valida os dados de entrada (DTOs)
 * e delega o processamento para a camada de serviço ({@link TransactionService}).</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Endpoints para criar e consultar movimentações financeiras")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Cria uma nova transação (Crédito ou Débito).
     *
     * <p>Este método recebe uma solicitação, valida os campos obrigatórios,
     * verifica regras de saldo e retorna o status adequado.</p>
     *
     * @param data DTO contendo os dados da transação (valor, tipo, conta).
     * @return ResponseEntity contendo o DTO de resposta e o status HTTP 201.
     */
    @Operation(summary = "Criar nova transação", description = "Registra um crédito ou débito em uma conta existente e atualiza o saldo automaticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: valor negativo, JSON inválido)"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente para operação de débito")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO data) {
        var response = transactionService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

