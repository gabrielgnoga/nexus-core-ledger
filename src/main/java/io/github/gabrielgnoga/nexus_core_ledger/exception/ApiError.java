package io.github.gabrielgnoga.nexus_core_ledger.exception;

import lombok.Data;
import java.time.LocalDateTime;
/**
 * Representação padronizada de erros da API.
 *
 * <p>Esta classe define o formato JSON que será retornado sempre que ocorrer uma exceção
 * no sistema. O objetivo é evitar respostas de erro inconsistentes ou que exponham
 * detalhes técnicos desnecessários (Stack Trace) para o cliente.</p>
 *
 * <p>Exemplo de saída JSON:</p>
 * <pre>
 * {
 * "timestamp": "2026-01-26T10:00:00",
 * "status": 404,
 * "error": "Recurso não encontrado",
 * "message": "Conta ID x não existe",
 * "path": "/api/accounts/x"
 * }
 * </pre>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Data
public class ApiError {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public ApiError() {
    }

    /**
     * Construtor completo para inicialização rápida do objeto de erro.
     * O timestamp é gerado automaticamente no momento da criação.
     *
     * @param status Código HTTP numérico.
     * @param error Título do erro.
     * @param message Mensagem explicativa.
     * @param path Caminho da requisição.
     */
    public ApiError(Integer status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}