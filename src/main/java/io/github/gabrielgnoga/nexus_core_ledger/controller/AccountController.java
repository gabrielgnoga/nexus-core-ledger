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
import java.util.UUID;

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
    /**
     * Endpoint para buscar uma conta pelo ID.
     *
     * <p>Exemplo de chamada: GET /api/accounts/a1b2-c3d4-...</p>
     *
     * @param id O UUID passado na URL.
     * @return 200 (OK) com a conta, ou 404 (Not Found) se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable UUID id) {
        return accountService.findAccountById(id)
                .map(account -> ResponseEntity.ok(account))
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Endpoint para deletar uma conta.
     *
     * <p>Retorna HTTP 204 (No Content) se der certo, indicando que o recurso não existe mais.</p>
     *
     * @param id O UUID passado na URL.
     * @return ResponseEntity vazio com status 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        accountService.deleteAccount(id);

        return ResponseEntity.noContent().build();
    }
}