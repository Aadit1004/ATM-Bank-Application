package persistence;

import model.AtmWorkRoom;
import model.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads a workroom for json file stored in data
public class JsonReader {
    private String source;

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public AtmWorkRoom read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: parses workroom from JSON object and returns it
    private AtmWorkRoom parseWorkRoom(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        AtmWorkRoom wr = new AtmWorkRoom(name);
        double chequingValue = jsonObject.getDouble("Chequings Balance");
        wr.addChequingBalanceToWorkRoom(chequingValue);
        double savingValue = jsonObject.getDouble("Savings Balance");
        wr.addSavingBalanceToWorkRoom(savingValue);
        addChequingsTransactions(wr, jsonObject);
        addSavingsTransactions(wr, jsonObject);
        return wr;
    }

    // REQUIRES: --
    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addChequingsTransactions(AtmWorkRoom wr, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Chequings Account Transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransactionJson = (JSONObject) json;
            String operation = nextTransactionJson.getString("Operation");
            Double transAmount = nextTransactionJson.getDouble("Transaction Amount");
            String takenFrom = nextTransactionJson.getString("Taken From");
            String sentTo = nextTransactionJson.getString("Sent to");
            Transaction nextTransaction = new Transaction(operation, transAmount, takenFrom, sentTo);
            wr.addChequingsTransactionToWorkRoom(nextTransaction);
        }
    }

    // REQUIRES: --
    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addSavingsTransactions(AtmWorkRoom wr, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Savings Account Transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransactionJson = (JSONObject) json;
            String operation = nextTransactionJson.getString("Operation");
            Double transAmount = nextTransactionJson.getDouble("Transaction Amount");
            String takenFrom = nextTransactionJson.getString("Taken From");
            String sentTo = nextTransactionJson.getString("Sent to");
            Transaction nextTransaction = new Transaction(operation, transAmount, takenFrom, sentTo);
            wr.addSavingsTransactionToWorkRoom(nextTransaction);
        }
    }
}