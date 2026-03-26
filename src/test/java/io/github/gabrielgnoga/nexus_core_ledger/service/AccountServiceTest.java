package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AccountResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.mapper.AccountMapper;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Test
    void deveCriarContaComSucesso() {
        // ARRANGE
        CreateAccountDTO requestDTO = new CreateAccountDTO();
        requestDTO.setName("Conta Poupança");

        Account contaSalva = new Account();
        contaSalva.setId(UUID.randomUUID());
        contaSalva.setName("Conta Poupança");

        AccountResponseDTO responseDTO = new AccountResponseDTO(
                contaSalva.getId(), "Conta Poupança", null, null, null
        );

        when(accountRepository.save(any(Account.class))).thenReturn(contaSalva);
        when(accountMapper.toDTO(contaSalva)).thenReturn(responseDTO);

        // ACT
        AccountResponseDTO result = accountService.createAccount(requestDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("Conta Poupança", result.name());

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(accountMapper, times(1)).toDTO(contaSalva);
    }

    @Test
    void deveLancarExcecaoAoTentarAtualizarContaInexistente() {
        // ARRANGE
        UUID idInvalido = UUID.randomUUID();
        CreateAccountDTO requestDTO = new CreateAccountDTO();
        requestDTO.setName("Novo Nome");

        when(accountRepository.findById(idInvalido)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.updateAccount(idInvalido, requestDTO);
        });

        verify(accountRepository, never()).save(any());
    }

    @Test
    void deveDeletarContaComSucessoQuandoElaExistir() {
        // ARRANGE
        UUID idValido = UUID.randomUUID();
        when(accountRepository.existsById(idValido)).thenReturn(true);

        // ACT
        accountService.deleteAccount(idValido);

        // ASSERT
        verify(accountRepository, times(1)).deleteById(idValido);
    }
}