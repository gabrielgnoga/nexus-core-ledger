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

        // 2. Montagem da Entidade (Mapping)
        // Aqui transformamos o DTO (papelada) em uma Entidade Real (objeto do banco)
        Account newAccount = new Account();
        newAccount.setName(dto.getName());

        // Convertendo a String do DTO para o Enum AccountType
        // Se o usuário mandar algo errado, o Java estoura erro automaticamente aqui
        newAccount.setAccountType(AccountType.valueOf(dto.getAccountType().toUpperCase()));

        // Regra definida: Toda conta nasce com saldo 0
        newAccount.setBalance(BigDecimal.ZERO);

        // 3. Salvar no Banco
        return accountRepository.save(newAccount);
    }
}