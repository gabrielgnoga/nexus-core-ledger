package io.github.gabrielgnoga.nexus_core_ledger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de teste para validar a segurança das rotas (A nossa "Sala Cofre").
 * * @author Gabriel Gnoga
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> secretRoute() {
        return ResponseEntity.ok("Acesso Liberado! Você está na área VIP da API e seu crachá é válido! ");
    }

    @GetMapping("/erro")
    public ResponseEntity<String> forceError() {
        // Simulando que o usuário tentou buscar algo que não existe
        throw new IllegalArgumentException("Testando o nosso para-quedas de erro global! O recurso não foi encontrado.");
    }

}