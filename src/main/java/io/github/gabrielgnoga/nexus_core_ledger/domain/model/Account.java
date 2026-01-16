package io.github.gabrielgnoga.nexus_core_ledger.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa a entidade de Conta Financeira (Ledger Account) no sistema.
 *
 * <p>Esta classe mapeia a tabela <code>accounts</code> no banco de dados e serve como
 * a fonte única da verdade para o estado financeiro de um registro.</p>
 *
 * <p>Características Técnicas:</p>
 * <ul>
 * <li>Utiliza <code>UUID</code> como chave primária para segurança e escalabilidade.</li>
 * <li>Armazena valores monetários com precisão de 4 casas decimais (Scale 4) para evitar erros de arredondamento.</li>
 * </ul>
 *
 * @author Gabriel Gnoga
 * @see AccountType
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Callback de ciclo de vida do JPA executado automaticamente antes da persistência (INSERT).
     *
     * <p>Este método garante a integridade dos dados aplicando valores padrão:</p>
     * <ul>
     * <li>Define a data de criação ({@code createdAt}) para o momento atual.</li>
     * <li>Inicializa o saldo ({@code balance}) como ZERO se estiver nulo.</li>
     * <li>Define a moeda ({@code currency}) como "BRL" se não for informada.</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.balance == null) this.balance = BigDecimal.ZERO;
        if (this.currency == null) this.currency = "BRL"; // Garante um padrão
    }
}