package persistence;

import model.AtmWorkRoom;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes the workroom to a json file
// CITATION: used from demo to figure out how to open and write and close.
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS: opens writer; throws exceptions FileNotFoundException if the file can't be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(AtmWorkRoom workroom) {
        JSONObject json = workroom.toJson();
        saveToFile(json.toString(TAB));
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}