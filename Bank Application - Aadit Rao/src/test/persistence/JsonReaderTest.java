package persistence;

import model.Transaction;
import model.AtmWorkRoom;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderGeneralAtmWorkRoom() {
    JsonReader reader = new JsonReader("./data/testReaderGeneralAtmWorkRoom.json");
    try {
        AtmWorkRoom wr = reader.read();
        assertEquals("Test AtmWorkRoom", wr.getName());
        List<Transaction> chequingsTransactionsList = wr.getChequingsTransactionsList();
        assertEquals(1, chequingsTransactionsList.size());
        List<Transaction> savingsTransactionsList = wr.getSavingsTransactionsList();
        assertEquals(1, savingsTransactionsList.size());
        checkTransaction("Withdraw", 100.00, "Chequings", "User", chequingsTransactionsList.get(0));
        checkTransaction("Deposit", 100.00, "User", "Savings", savingsTransactionsList.get(0));
        assertEquals(1000, wr.getChequingsBalance());
        assertEquals(5000, wr.getSavingsBalance());
    } catch (IOException e) {
        fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEmptyAtmWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAtmWorkRoom.json");
        try {
            AtmWorkRoom wr = reader.read();
            assertEquals("Test AtmWorkRoom", wr.getName());
            assertEquals(0, wr.numChequingsTransactions());
            assertEquals(0, wr.numSavingsTransactions());
            assertEquals(0, wr.getSavingsBalance());
            assertEquals(0, wr.getChequingsBalance());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try{
            AtmWorkRoom wr = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
}