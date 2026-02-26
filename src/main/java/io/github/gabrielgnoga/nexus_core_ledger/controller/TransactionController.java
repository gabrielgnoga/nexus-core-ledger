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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    /**
     * Recupera o extrato bancário de uma conta específica.
     *
     * <p>Busca todo o histórico de transações (créditos e débitos) vinculados
     * ao ID da conta informado. A lista é retornada em ordem cronológica inversa
     * (da transação mais recente para a mais antiga).</p>
     *
     * @param accountId O identificador único (UUID) da conta bancária.
     * @return ResponseEntity contendo a lista de transações (DTOs) e o status HTTP 200 (OK).
     */
    @Operation(summary = "Obter extrato da conta", description = "Retorna o histórico completo de transações de uma conta, ordenado da mais recente para a mais antiga.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Extrato recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getStatement(@PathVariable UUID accountId) {
        var response = transactionService.getStatement(accountId);
        return ResponseEntity.ok(response);
    }
}