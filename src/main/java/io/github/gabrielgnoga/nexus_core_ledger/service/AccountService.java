package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(CreateAccountDTO dto) {
        if (accountRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Já existe uma conta com o nome: " + dto.getName());
        }

        Account newAccount = Account.builder()
                .name(dto.getName())
                .accountType(AccountType.valueOf(dto.getAccountType().toUpperCase()))
                .balance(BigDecimal.ZERO) // balance starts on zero
                .currency("BRL")          // setting a pattern currency
                .build();                 // creates the final object and closes its package

        return accountRepository.save(newAccount);
    }
}