package io.github.gabrielgnoga.nexus_core_ledger.domain.model;

/**
 * Define os tipos de contas contábeis permitidas no sistema (Plano de Contas).
 *
 * <p>Baseado nos 5 pilares da contabilidade moderna:</p>
 * <ul>
 * <li>**ASSET (Ativo):** Bens e direitos. Ex: Caixa, Banco, Carro.</li>
 * <li>**LIABILITY (Passivo):** Deveres e obrigações. Ex: Cartão de Crédito, Financiamento.</li>
 * <li>**EQUITY (Patrimônio Líquido):** A riqueza real (Ativos - Passivos).</li>
 * <li>**REVENUE (Receita):** Entrada de dinheiro. Ex: Salário, Venda de Carro.</li>
 * <li>**EXPENSE (Despesa):** Saída de dinheiro. Ex: Gasolina, Aluguel.</li>
 * </ul>
 *
 * @author Gabriel Gnoga
 * @since 1.0.0
 */
public enum AccountType {
    ASSET,
    LIABILITY,
    EQUITY,
    REVENUE,
    EXPENSE
}