package io.github.gabrielgnoga.nexus_core_ledger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exceção lançada quando uma tentativa de débito excede o saldo disponível na conta.
 *
 * <p>Esta exceção é mapeada automaticamente para o status HTTP 422 (Unprocessable Entity),
 * indicando que a requisição foi bem formada (sintaxe correta), mas não pôde ser processada
 * devido às regras de negócio (semântica).</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>
 * if (saldo.compareTo(valorSaque) < 0) {
 * throw new InsufficientBalanceException("Saldo insuficiente.");
 * }
 * </pre>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 * @see org.springframework.http.HttpStatus#UNPROCESSABLE_ENTITY
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InsufficientBalanceException extends RuntimeException {
    /**
     * Constrói a exceção com uma mensagem detalhada do erro.
     *
     * @param message A mensagem explicativa (ex: "Saldo insuficiente: Atual=10, Saque=50")
     */
    public InsufficientBalanceException(String message) {
        super(message);
    }
}