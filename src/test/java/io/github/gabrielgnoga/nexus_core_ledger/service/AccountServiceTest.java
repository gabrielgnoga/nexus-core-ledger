package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AccountResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.mapper.AccountMapper;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * Testes unitários para a classe {@link AccountService}.
 * <p>
 * Valida as regras de negócio isoladamente usando Mocks para o repositório.
 * </p>
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("Deve criar uma conta com sucesso e retornar o DTO correto")
    void createAccount_ShouldReturnDto_WhenDataIsValid() {
        CreateAccountDTO inputDto = new CreateAccountDTO();
        inputDto.setName("Conta Nubank");
        inputDto.setAccountType("ASSET");

        Account savedAccount = new Account();
        savedAccount.setId(UUID.randomUUID());
        savedAccount.setName("Conta Nubank");
        savedAccount.setAccountType(AccountType.ASSET);
        savedAccount.setBalance(BigDecimal.ZERO);

        // O que o mapper "deveria" retornar
        AccountResponseDTO expectedDto = new AccountResponseDTO(
                savedAccount.getId(), "Conta Nubank", BigDecimal.ZERO, AccountType.ASSET, null
        );


        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(expectedDto);
        // executar
        AccountResponseDTO result = accountService.createAccount(inputDto);

        // validar
        assertNotNull(result);
        assertEquals("Conta Nubank", result.getName());
        assertEquals(AccountType.ASSET, result.getAccountType());

        // verificar o save
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Deve retornar Vazio quando buscar um ID que não existe")
    void findAccountById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        // --- ARRANGE ---
        UUID idFalso = UUID.randomUUID();

        // caso esteja vazio
        when(accountRepository.findById(idFalso)).thenReturn(Optional.empty());

        // acao no service
        Optional<AccountResponseDTO> result = accountService.findAccountById(idFalso);

        // verificar
        assertTrue(result.isEmpty()); // Tem que estar vazio
        verify(accountRepository, times(1)).findById(idFalso);
    }
}