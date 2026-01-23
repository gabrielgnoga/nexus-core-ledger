package io.github.gabrielgnoga.nexus_core_ledger.service;

import io.github.gabrielgnoga.nexus_core_ledger.domain.model.Account;
import io.github.gabrielgnoga.nexus_core_ledger.domain.model.AccountType;
import io.github.gabrielgnoga.nexus_core_ledger.dto.CreateAccountDTO;
import io.github.gabrielgnoga.nexus_core_ledger.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada a Contas.
 *
 * <p>Esta classe atua como uma barreira de integridade, garantindo que
 * nenhuma conta seja persistida sem atender às regras de validação de negócios,
 * como unicidade de nomes e inicialização correta de saldos.</p>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Cria uma nova conta financeira no sistema.
     *
     * <p>Este método executa as seguintes etapas:</p>
     * <ol>
     * <li>Verifica se já existe uma conta com o mesmo nome (Regra de Unicidade).</li>
     * <li>Converte o DTO para a Entidade {@link Account} usando o padrão Builder.</li>
     * <li>Normaliza o tipo de conta (toUpperCase) para evitar erros de enum.</li>
     * <li>Persiste o registro no banco de dados.</li>
     * </ol>
     *
     * <p>O método é executado dentro de uma transação ({@code @Transactional}).
     * Se qualquer erro ocorrer, todas as operações no banco de dados serão revertidas (Rollback).</p>
     *
     * @param dto O objeto contendo os dados validados de entrada.
     * @return A entidade {@link Account} persistida com ID gerado e timestamp.
     * @throws IllegalArgumentException se já existir uma conta com o mesmo nome.
     */
    @Transactional
    public Account createAccount(CreateAccountDTO dto) {
        if (accountRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Já existe uma conta com este nome: " + dto.getName());
        }

        Account newAccount = Account.builder()
                .name(dto.getName())
                .accountType(AccountType.valueOf(dto.getAccountType().toUpperCase()))
                .build();

        return accountRepository.save(newAccount);
    }
    /**
     * Recupera todas as contas cadastradas no sistema.
     *
     * <p>Utiliza o método padrão {@code findAll()} do JPA para buscar todos os registros.</p>
     *
     * @return Uma lista contendo todas as contas encontradas. Retorna lista vazia se não houver nada.
     */
    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }
    /**
     * Busca uma conta específica pelo seu ID único.
     *
     * <p>Retorna um {@link Optional} porque a conta pode não existir no banco.
     * Isso obriga quem chamar este método a tratar o cenário de "Não Encontrado".</p>
     *
     * @param id O UUID da conta.
     * @return Um Optional contendo a conta (se existir) ou vazio.
     */
    public Optional<Account> findAccountById(UUID id) {
        return accountRepository.findById(id);
    }
    /**
     * Exclui uma conta do banco de dados.
     *
     * <p>Primeiro verifica se o ID existe. Se não existir, lança um erro
     * Se existir, manda o repositório apagar.</p>
     *
     * @param id O UUID da conta a ser excluída.
     */
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Conta não encontrada para exclusão.");
        }
        accountRepository.deleteById(id);
    }
    /**
     * Atualiza os dados de uma conta existente.
     *
     * <p>Regra de Ouro: Buscamos a conta primeiro para garantir que o ID existe
     * e para manter os dados que NÃO podem mudar (como id, createdAt e balance).</p>
     *
     * @param id O UUID da conta a ser atualizada.
     * @param dto Os novos dados (Nome, Tipo).
     * @return A conta atualizada.
     */
    public Account updateAccount(UUID id, CreateAccountDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        account.setName(dto.getName());
        account.setAccountType(AccountType.valueOf(dto.getAccountType().toUpperCase()));

        return accountRepository.save(account);
    }
}