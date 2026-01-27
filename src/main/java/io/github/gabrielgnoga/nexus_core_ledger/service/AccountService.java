package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.AccountResponseDTO;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.mapper.AccountMapper;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    // 1. CREATE
    public AccountResponseDTO createAccount(CreateAccountDTO dto) {
        Account account = new Account();
        account.setName(dto.getName());
        account.setAccountType(AccountType.valueOf(dto.getAccountType().toUpperCase()));

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDTO(savedAccount);
    }

    // 2. LIST ALL
    public List<AccountResponseDTO> listAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 3. FIND BY ID
    public Optional<AccountResponseDTO> findAccountById(UUID id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDTO);
    }

    // 4. UPDATE
    public AccountResponseDTO updateAccount(UUID id, CreateAccountDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        account.setName(dto.getName());
        account.setAccountType(AccountType.valueOf(dto.getAccountType().toUpperCase()));

        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toDTO(updatedAccount);
    }

    // 5. DELETE
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }
        accountRepository.deleteById(id);
    }
}