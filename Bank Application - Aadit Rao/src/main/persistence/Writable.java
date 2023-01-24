package persistence;


import org.json.JSONObject;

// CITATION: borrowed from JSON demo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}