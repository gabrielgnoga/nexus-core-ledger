package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserRepository userRepository;

    @Test
    void deveRetornarUsuarioQuandoLoginExistir() {

        // ARRANGE
        String loginProcurado = "gabriel.dev";

        UserDetails usuarioFalso = mock(UserDetails.class);

        when(userRepository.findByLogin(loginProcurado)).thenReturn(usuarioFalso);

        // ACT
        UserDetails resultado = authorizationService.loadUserByUsername(loginProcurado);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(usuarioFalso, resultado);

        verify(userRepository, times(1)).findByLogin(loginProcurado);
    }
}