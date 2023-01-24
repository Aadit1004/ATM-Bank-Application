package model;

import org.json.JSONObject;
import persistence.Writable;

// This class represents a transaction with an operation, a transaction amount, where it was taken from, and
// where was it sent to
public class Transaction implements Writable {

    private String operation;        // the operation of the transaction
    private double transAmount;      // the amount of the transaction
    private String takenFrom;        // from where the money got withdrawn from
    private String sentTo;           // from where the money got deposited to

    // REQUIRES: transAmount >= 0
    // MODIFIES: --
    // EFFECTS: Constructs a transaction with an operation, trans amount, where the money was taken from and where
    // was it sent to
    public Transaction(String operation, double transAmount, String takenFrom, String sentTo) {
        this.operation = operation;
        if (transAmount < 0) {
            transAmount = 0;
        }
        this.transAmount = transAmount;
        this.takenFrom = takenFrom;
        this.sentTo = sentTo;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns the operation of the transaction
    public String getOperation() {
        return operation;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns the operation of the transaction
    public double getTransAmount() {
        return transAmount;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns the operation of the transaction
    public String getTakenFrom() {
        return takenFrom;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns the operation of the transaction
    public String getSentTo() {
        return sentTo;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Converts the transaction components to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Operation", operation);
        json.put("Transaction Amount", transAmount);
        json.put("Taken From", takenFrom);
        json.put("Sent to", sentTo);
        return json;
    }
}