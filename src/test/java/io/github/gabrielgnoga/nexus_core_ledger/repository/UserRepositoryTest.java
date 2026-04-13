package io.github.gabrielgnoga.nexus_core_ledger.repository;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveEncontrarUsuarioPorLoginComSucesso() {
        // ARRANGE
        String emailAlvo = "admin@nexus.com";

        User usuario = new User(emailAlvo, "hashDaSenha123");

        entityManager.persistAndFlush(usuario);

        // ACT
        UserDetails resultado = userRepository.findByLogin(emailAlvo);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo(emailAlvo);
    }

    @Test
    void deveRetornarNullQuandoLoginNaoExistir() {
        // ARRANGE
        String emailInexistente = "ghost@nexus.com";

        // ACT
        UserDetails resultado = userRepository.findByLogin(emailInexistente);

        // ASSERT
        assertThat(resultado).isNull();
    }
}