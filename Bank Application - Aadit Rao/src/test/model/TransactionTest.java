package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private Transaction testTransaction;

    @BeforeEach
    void beforeEach() {
        testTransaction = new Transaction("Withdraw", 100.00,
                "Savings", "User");
    }

    @Test
    void testTransaction() {
        assertEquals("Withdraw", testTransaction.getOperation());
        assertEquals(100.00, testTransaction.getTransAmount());
        assertEquals("Savings", testTransaction.getTakenFrom());
        assertEquals("User", testTransaction.getSentTo());
    }

    @Test
    void testTransactionNegAmt() {
        Transaction testTransaction2 = new Transaction("Deposit", -100.00,
                "User", "Savings");
        assertEquals("Deposit", testTransaction2.getOperation());
        assertEquals(0, testTransaction2.getTransAmount());
        assertEquals("User", testTransaction2.getTakenFrom());
        assertEquals("Savings", testTransaction2.getSentTo());
    }
}