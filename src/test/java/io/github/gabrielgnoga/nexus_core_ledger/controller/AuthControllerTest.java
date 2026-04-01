package io.github.gabrielgnoga.nexus_core_ledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AuthenticationDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.RegisterDTO;
import io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository;
import io.github.gabrielgnoga.nexus_core_ledger.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository repository;

    @Test
    void deveRealizarLoginComSucessoERetornarToken() throws Exception {

        // ARRANGE
        AuthenticationDTO loginDTO = new AuthenticationDTO("gabriel.dev", "senha123");
        User usuarioSimulado = new User("gabriel.dev", "senhaCriptografada");
        String tokenSimulado = "eyJh...token...falso";

        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(usuarioSimulado);

        when(authenticationManager.authenticate(any())).thenReturn(authenticationMock);
        when(tokenService.generateToken(usuarioSimulado)).thenReturn(tokenSimulado);

        String jsonPayload = objectMapper.writeValueAsString(loginDTO);

        // ACT & ASSERT
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(tokenSimulado));
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        // ARRANGE
        RegisterDTO registerDTO = new RegisterDTO("novo.usuario", "senha123");

        when(repository.findByLogin(registerDTO.login())).thenReturn(null);

        String jsonPayload = objectMapper.writeValueAsString(registerDTO);

        // ACT & ASSERT
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))

                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarBadRequestQuandoTentarRegistrarLoginQueJaExiste() throws Exception {

        // ARRANGE
        RegisterDTO registerDTO = new RegisterDTO("gabriel.dev", "senha123");
        User usuarioJaExistente = new User("gabriel.dev", "senhaAntiga");

        when(repository.findByLogin(registerDTO.login())).thenReturn(usuarioJaExistente);

        String jsonPayload = objectMapper.writeValueAsString(registerDTO);

        // ACT & ASSERT
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))

                .andExpect(status().isBadRequest());
    }
}