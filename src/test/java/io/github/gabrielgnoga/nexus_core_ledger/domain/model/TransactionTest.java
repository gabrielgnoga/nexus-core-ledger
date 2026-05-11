package io.github.gabrielgnoga.nexus_core_ledger.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void devePreencherTimestampAutomaticamenteAoCriarTransacao() {
        Transaction transaction = new Transaction();
        transaction.onCreate();
        assertNotNull(transaction.getTimestamp(), "O timestamp não deveria ser nulo ao instanciar uma Transaction");
    }
}