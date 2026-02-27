package io.github.gabrielgnoga.nexus_core_ledger.repository;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

/**
 * Repositório responsável pelo acesso aos dados dos usuários no banco.
 *
 * <p>Integra-se com o Spring Data JPA para operações CRUD e fornece
 * consultas customizadas exigidas pelo Spring Security para autenticação.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca um usuário no banco de dados utilizando o login (e-mail).
     * * <p>Este método é o motor principal da autenticação. O Spring Security
     * usa esse retorno para comparar a senha digitada com a senha "triturada" (Hash)
     * salva no banco.</p>
     *
     * @param login O e-mail (username) do usuário tentando logar.
     * @return Um objeto que assina o contrato UserDetails com os dados do usuário.
     */
    UserDetails findByLogin(String login);
}