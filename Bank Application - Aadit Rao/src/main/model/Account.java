package model;

import java.util.ArrayList;

// This class represents an Account with a balance, a name, and a list of transactions connected to that account
public class Account {

    private double balance;                        // the balance of the account
    private final String name;                     // the account name
    private ArrayList<Transaction> transactions;   // the list of transaction that has happened with that account

    // REQUIRES: balance >= 0
    // MODIFIES: --
    // EFFECTS: Constructs an Account with given name and balance where balance>=0
    public Account(double balance, String name, ArrayList<Transaction> transactions) {
        if (balance >= 0) {
            this.balance = balance;
        } else {
            this.balance = 0;
        }
        this.name = name;
        this.transactions = transactions;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns balance of the Account as a double
    public double getBalance() {
        return balance;
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: returns name of Account as a String
    public String getName() {
        return name;
    }

    // REQUIRES: depositAmount >= 0
    // MODIFIES: this
    // EFFECTS: adds depositAmount to the balance
    public double deposit(double depositAmount) {
        if (depositAmount >= 0) {
            balance = balance + depositAmount;
            EventLog.getInstance().logEvent(new Event("Deposited $" + depositAmount + " to " + this.name));
        }
        return balance;
    }

    // REQUIRES: withdrawAmount >= 0
    // MODIFIES: this
    // EFFECTS:  withdraws withdrawAmount from balance
    public double withdraw(double withdrawAmount) {
        if (withdrawAmount >= 0) {
            balance = balance - withdrawAmount;
            EventLog.getInstance().logEvent(new Event("Withdrew $" + withdrawAmount + " to " + this.name));
        }
        return balance;
    }

    // REQUIRES: --
    // MODIFIES: this
    // EFFECTS:  adds a transaction to the transaction list
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        EventLog.getInstance().logEvent(new Event("Transaction added to: " + this.name));
    }
}