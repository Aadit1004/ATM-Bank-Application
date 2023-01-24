package persistence;

import model.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkTransaction(String operation, double transAmount, String takenFrom, String sentTo, Transaction transaction) {
        assertEquals(operation, transaction.getOperation());
        assertEquals(transAmount, transaction.getTransAmount());
        assertEquals(takenFrom, transaction.getTakenFrom());
        assertEquals(sentTo, transaction.getSentTo());
    }
}