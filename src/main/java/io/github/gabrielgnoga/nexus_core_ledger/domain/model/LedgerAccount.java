package io.github.gabrielgnoga.nexus_core_ledger.domain.model;

public class LedgerAccount {
    // ISCA 1: Uso de ponto flutuante (double) para valores
    private double balance;

    public LedgerAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    // ISCA 2: Condição de Corrida (Race Condition)
    public void processWithdrawal(double amount) {
        // Se duas threads entrarem aqui ao mesmo tempo, ambas passam pelo IF
        if (balance >= amount) {
            try {
                // Simulando o tempo de ida ao banco de dados
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // O saldo ficará negativo se as threads executarem simultaneamente
            balance = balance - amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}
