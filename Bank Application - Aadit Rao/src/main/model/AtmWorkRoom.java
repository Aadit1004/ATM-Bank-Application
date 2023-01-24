package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents an ATM workroom having a collection of transactions and balance
public class AtmWorkRoom implements Writable {
    private double chequingsBalance;
    private double savingsBalance;
    private ArrayList<Transaction> chequingsTransactionsList;
    private ArrayList<Transaction> savingsTransactionsList;
    private String name;

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Constructs workroom with a name, empty list of transactions, and
    // balance of 0
    public AtmWorkRoom(String name) {
        this.name = name;
        this.chequingsBalance = 0;
        this.savingsBalance = 0;
        this.chequingsTransactionsList = new ArrayList<>();
        this.savingsTransactionsList = new ArrayList<>();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Gets name of workroom
    public String getName() {
        return name;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Gets Chequings Balance of workroom
    public double getChequingsBalance() {
        return chequingsBalance;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Gets Savings Balance of workroom
    public double getSavingsBalance() {
        return savingsBalance;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Gets list of Chequings transactions
    public ArrayList<Transaction> getChequingsTransactionsList() {
        return chequingsTransactionsList;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Gets list of Savings transactions
    public ArrayList<Transaction> getSavingsTransactionsList() {
        return savingsTransactionsList;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns number of transactions for Chequings Account in this workroom
    public int numChequingsTransactions() {
        return chequingsTransactionsList.size();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns number of transactions for Savings Account in this workroom
    public int numSavingsTransactions() {
        return savingsTransactionsList.size();
    }

    // REQUIRES: balance >= 0
    // MODIFIES: this
    // EFFECTS: adds the chequing account balance to the workroom
    public void addChequingBalanceToWorkRoom(double balance) {
        chequingsBalance = balance;
    }

    // REQUIRES: balance >= 0
    // MODIFIES: this
    // EFFECTS: adds the saving account balance to the workroom
    public void addSavingBalanceToWorkRoom(double balance) {
        savingsBalance = balance;
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS: adds the chequing transaction that has been done to the workroom
    public void addChequingsTransactionToWorkRoom(Transaction t) {
        chequingsTransactionsList.add(t);
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS: adds the saving transaction that has been done to the workroom
    public void addSavingsTransactionToWorkRoom(Transaction t) {
        savingsTransactionsList.add(t);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Converts the Workroom to JSON objects and Arrays
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("Chequings Balance", chequingsBalance);
        json.put("Savings Balance", savingsBalance);
        json.put("Chequings Account Transactions", chequingsTransactionsToJson());
        json.put("Savings Account Transactions", savingsTransactionsToJson());
        return json;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns transactions in chequings account in this workroom as a JSON array
    // CITATION: used from json demo to convert list of transactions to a jsonArray
    private JSONArray chequingsTransactionsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Transaction t : chequingsTransactionsList) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns transactions in savings account in this workroom as a JSON array
    private JSONArray savingsTransactionsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Transaction t : savingsTransactionsList) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }
}