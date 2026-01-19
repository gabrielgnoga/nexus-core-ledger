package io.github.gabrielgnoga.nexus_core_ledger.controller;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por expor os endpoints de gerenciamento de Contas.
 *
 * <p>Atua como a porta de entrada da API, recebendo requisições HTTP,
 * validando payloads JSON e delegando o processamento para o {@link AccountService}.</p>
 *
 * <p>Base URL: <code>/api/accounts</code></p>
 *
 * @author Gabriel Noga
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Endpoint para criação de uma nova conta.
     *
     * <p>Recebe um JSON, valida os campos obrigatórios e retorna a conta criada.</p>
     *
     * @param dto Payload JSON mapeado automaticamente. Requer validação (@Valid).
     * @return ResponseEntity contendo a conta criada e status HTTP 201 (Created).
     *
     */
    @PostMapping
    public ResponseEntity<Account> create(@RequestBody @Valid CreateAccountDTO dto) {
        Account newAccount = accountService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }
    /**
     * Endpoint para listar todas as contas.
     *
     * <p>Retorna uma lista JSON com todos os registros encontrados no banco.</p>
     *
     * @return ResponseEntity contendo a lista de contas e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Account>> listAll() {
        List<Account> accounts = accountService.listAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}