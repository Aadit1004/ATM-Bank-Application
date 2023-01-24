package persistence;

import model.AtmWorkRoom;
import model.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterGeneralAtmWorkroom() {
        try {
            AtmWorkRoom wr = new AtmWorkRoom("Test AtmWorkRoom");
            wr.addChequingBalanceToWorkRoom(1000);
            wr.addSavingBalanceToWorkRoom(5000);
            Transaction cheqTransaction = new Transaction("Withdraw", 100.00, "Chequings", "User");
            Transaction savTransaction = new Transaction("Deposit", 100.00, "User", "Savings");
            wr.addChequingsTransactionToWorkRoom(cheqTransaction);
            wr.addSavingsTransactionToWorkRoom(savTransaction);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAtmWorkRoom.json");
            writer.open();
            writer.write(wr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAtmWorkRoom.json");
            wr = reader.read();
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
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterEmptyAtmWorkroom() {
        try {
            AtmWorkRoom wr = new AtmWorkRoom("Test AtmWorkRoom");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyAtmWorkRoom.json");
            writer.open();
            writer.write(wr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAtmWorkRoom.json");
            wr = reader.read();
            assertEquals("Test AtmWorkRoom", wr.getName());
            assertEquals(0, wr.numChequingsTransactions());
            assertEquals(0, wr.numSavingsTransactions());
            assertEquals(0, wr.getSavingsBalance());
            assertEquals(0, wr.getChequingsBalance());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterInvalidFile() {
        try {
            AtmWorkRoom wr = new AtmWorkRoom("Test AtmWorkRoom");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }
}