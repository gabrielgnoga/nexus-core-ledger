package io.github.gabrielgnoga.nexus_core_ledger.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável por gerar e validar Tokens JWT.
 *
 * <p>Utiliza a biblioteca Auth0 para criar tokens com tempo de expiração
 * e validar a autenticidade de tokens recebidos nas requisições.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um novo Token JWT para o usuário autenticado.
     *
     * @param user O usuário que acabou de fazer login com sucesso.
     * @return O Token JWT em formato de String.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("nexus-core-ledger-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Lê o token, verifica se a assinatura é verdadeira e se não está vencido.
     * Se estiver tudo certo, devolve o e-mail do dono do token.
     *
     * @param token O Token JWT recebido no cabeçalho da requisição.
     * @return O login (e-mail) do usuário, ou uma string vazia se o token for inválido.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("nexus-core-ledger-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {

            return "";
        }
    }

    /**
     * Define a validade do Token (Ex: 2 horas a partir de agora).
     */
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}