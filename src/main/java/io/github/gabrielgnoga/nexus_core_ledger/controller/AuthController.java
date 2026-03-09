package io.github.gabrielgnoga.nexus_core_ledger.controller;

import io.github.gabrielgnoga.nexus_core_ledger.dto.AuthenticationDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.LoginResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.User;
import io.github.gabrielgnoga.nexus_core_ledger.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pelos endpoints públicos de Autenticação.
 * É a vitrine onde o usuário apresenta as credenciais para receber o Token JWT.
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private io.github.gabrielgnoga.nexus_core_ledger.repository.UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid io.github.gabrielgnoga.nexus_core_ledger.dto.RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(data.login(), encryptedPassword);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}