package io.github.gabrielgnoga.nexus_core_ledger.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Manipulador Global de Exceções (Global Exception Handler).
 *
 * <p>Esta classe atua como um interceptador central para todas as exceções lançadas
 * pelos Controllers da aplicação. Ela utiliza a anotação {@link RestControllerAdvice}
 * para capturar erros e transformá-los em respostas JSON padronizadas usando a classe {@link ApiError}.</p>
 *
 * <p>Benefícios:</p>
 * <ul>
 * <li>Evita expor Stack Traces sensíveis para o cliente.</li>
 * <li>Padroniza o formato de erro em toda a API.</li>
 * <li>Centraliza a lógica de decisão de Status HTTP.</li>
 * </ul>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura exceções de argumento inválido ({@link IllegalArgumentException}).
     *
     * <p>Geralmente lançada pelo Service quando um recurso solicitado não é encontrado
     * (ex: buscar um ID inexistente) ou quando uma validação de regra de negócio falha.</p>
     *
     * @param ex A exceção capturada contendo a mensagem de erro original.
     * @param request A requisição HTTP original, usada para extrair o caminho (URI).
     * @return Um {@link ResponseEntity} contendo o {@link ApiError} com status 404 (Not Found).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError apiError = new ApiError(
                status.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiError);
    }

    /**
     * Captura genérica para qualquer exceção não tratada especificamente (Fallback).
     *
     * <p>Serve como uma rede de segurança para erros inesperados (NullPointer, falha de banco, etc).
     * Retorna sempre um erro genérico para não vazar detalhes de infraestrutura.</p>
     *
     * @param ex A exceção inesperada.
     * @param request A requisição HTTP original.
     * @return Um {@link ResponseEntity} contendo o {@link ApiError} com status 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError apiError = new ApiError(
                status.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado. Por favor, contate o suporte.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiError);
    }
    /**
     * Intercepta a exceção de Saldo Insuficiente e formata a resposta HTTP.
     *
     * <p>Impede que o sistema retorne um Erro 500 (Internal Server Error) genérico
     * quando uma regra de negócio é violada. Em vez disso, devolve um status
     * 422 (Unprocessable Entity) com os detalhes exatos do bloqueio da transação.</p>
     *
     * @param e A exceção capturada contendo a mensagem de erro original da camada de Serviço.
     * @param request O objeto da requisição HTTP, usado para extrair o caminho (URI) onde a falha ocorreu.
     * @return Um ResponseEntity contendo o objeto de erro padronizado em JSON e o status 422.
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> handleInsufficientBalance (InsufficientBalanceException e, HttpServletRequest request) {
        ApiError err = new ApiError();
        err.setTimestamp(LocalDateTime.now());
        err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value()); // 422
        err.setError("Regra de Negócio / Saldo Insuficiente");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }
}
