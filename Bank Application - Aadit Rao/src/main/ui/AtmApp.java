package ui;

import model.Account;
import model.AtmWorkRoom;
import model.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.stream.Stream;

// ATM console application
public class AtmApp {

    private final int accountID = 12345;
    private final int pin = 777;
    private ArrayList<Transaction> chequingTransactionsList = new ArrayList<>();
    private ArrayList<Transaction> savingTransactionsList = new ArrayList<>();
    private Account chequingsAccount = new Account(5000.00, "Chequing", chequingTransactionsList);
    private Account savingsAccount = new Account(1000.00, "Savings", savingTransactionsList);
    private static final String JSON_STORE = "./data/workroom.json";
    private AtmWorkRoom workRoom;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Starts the atm calling the StartApp method
    public AtmApp() {
        startApp();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: initiates the ATM application
    // CITATION: used https://www.w3schools.com/java/java_user_input.asp to see how to get input from User
    // and how to use the Scanner function.
    public void startApp() {
        Scanner login = new Scanner(System.in);
        System.out.println("Please enter your Account ID:");
        int userID = login.nextInt();
        System.out.println("Please enter your pin:");
        int userPin = login.nextInt();
        if ((accountID == userID) && (pin == userPin)) {
            System.out.println("Successfully logged in to your Account");
            workRoom = new AtmWorkRoom("ATM WorkRoom");
            jsonWriter = new JsonWriter(JSON_STORE);
            jsonReader = new JsonReader(JSON_STORE);
            chooseToLoad();
            while (true) {
                startAppHelper();
            }
        } else {
            System.out.println("Incorrect ID or pin");
            System.exit(0);
        }
    }

    // REQUIRES: --
    // MODIFIES: wr
    // EFFECTS: asks the option if the person wants to load transactions and balance to the ATM
    private void chooseToLoad() {
        Scanner choiceSel = new Scanner(System.in);
        System.out.println("Would you like to load your transactions and balance to the ATM?");
        System.out.println("1. Yes\n2. No");
        int selection = choiceSel.nextInt();
        if (selection == 1) {
            loadAtmWorkRoom();
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Is a helper function that displays the operations of the ATM
    private void startAppHelper() {
        Scanner choiceSel = new Scanner(System.in);
        System.out.println("Please select an operation:");
        System.out.println(choices());
        int selection = choiceSel.nextInt();
        if (selection == 1) {
            withdrawOperation();
        } else if (selection == 2) {
            depositOperation();
        } else if (selection == 3) {
            transferOperation();
        } else if (selection == 4) {
            showTransactions();
        } else if (selection == 5) {
            printBalances();
        } else if (selection == 6) {
            saveAtmWorkRoom();
        } else if (selection == 7) {
            loadAtmWorkRoom();
        } else if (selection == 8) {
            quitAtm();
        } else {
            System.out.println("Please choose a valid option.");
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Prints the choices you can operate with the ATM
    private String choices() {
        return "1. Withdraw Amount\n" + "2. Deposit Amount\n" + "3. Transfer Amount\n" + "4. Transaction History\n"
                + "5. Check Available Balance\n" + "6. Save Transactions and Balance\n"
                + "7. Load Transactions and Balance\n" + "8. Exit ATM";
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Quits the Atm application with the option to save state of ATM
    private void quitAtm() {
        System.out.println("Would you like to save your transactions and balance to the account?");
        Scanner choiceSel = new Scanner(System.in);
        System.out.println("1. Yes\n2. No");
        int selection = choiceSel.nextInt();
        if (selection == 1) {
            saveAtmWorkRoom();
        }
        System.out.println("Thank you for using the ATM!");
        System.exit(0);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Prints the chequings and savings balances
    private void printBalances() {
        System.out.println("Your Chequings Account balance is $" + chequingsAccount.getBalance());
        System.out.println("Your Savings Account balance is $" + savingsAccount.getBalance());
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Allows the user to withdraw an amount from either Account
    private void withdrawOperation() {
        Scanner accountSel = new Scanner(System.in);
        System.out.println("Choose which account to withdraw from:\n"
                + "1. Chequings Account\n"
                + "2. Savings Account");
        int selection = accountSel.nextInt();
        if (selection == 1) {
            withdrawHelper1();
        } else if (selection == 2) {
            withdrawHelper2();
        } else {
            System.out.println("Please select a valid option.");
        }
    }

    // REQUIRES: withdrawAmt >= 0 && withdrawAmt <= balance of Chequings Account
    // MODIFIES: chequingsAccount balance and transaction list
    // EFFECTS: allows user to withdraw an amount from the chequings account and the transaction gets added
    // to the transaction list of the Chequings account
    private void withdrawHelper1() {
        Scanner amount = new Scanner(System.in);
        System.out.println("Enter the amount you would like to withdraw:");
        double withdrawAmt = amount.nextDouble();
        if ((withdrawAmt >= 0) && (withdrawAmt <= chequingsAccount.getBalance())) {
            chequingsAccount.withdraw(withdrawAmt);
            Transaction withdrawTransaction = new Transaction("Withdraw", withdrawAmt,
                    "Chequings", "User");
            chequingsAccount.addTransaction(withdrawTransaction);
            workRoom.addChequingsTransactionToWorkRoom(withdrawTransaction);
            System.out.println("Transaction Successful. \n"
                    + "You current balance in your Chequings Account is: $" + chequingsAccount.getBalance());
        } else if ((withdrawAmt > chequingsAccount.getBalance())) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("You can't withdraw a negative amount. Please try again.");
        }
    }

    // REQUIRES: withdrawAmt >= 0 && withdrawAmt <= balance of Savings Account
    // MODIFIES: savingsAccount balance and transaction list
    // EFFECTS: allows user to withdraw an amount from the savings account and the transaction gets added
    // to the transaction list of the Savings account
    private void withdrawHelper2() {
        Scanner amount2 = new Scanner(System.in);
        System.out.println("Enter the amount you would like to withdraw:");
        double withdrawAmt = amount2.nextDouble();
        if ((withdrawAmt >= 0) && (withdrawAmt <= savingsAccount.getBalance())) {
            savingsAccount.withdraw(withdrawAmt);
            Transaction withdrawTransaction = new Transaction("Withdraw", withdrawAmt,
                    "Savings", "User");
            savingsAccount.addTransaction(withdrawTransaction);
            workRoom.addSavingsTransactionToWorkRoom(withdrawTransaction);
            System.out.println("Transaction Successful. \n"
                    + "You current balance in your Savings Account is: $" + savingsAccount.getBalance());
        } else if ((withdrawAmt > savingsAccount.getBalance())) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("You can't withdraw a negative amount. Please try again.");
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Allows the user to deposit an amount from either Account
    private void depositOperation() {
        Scanner accountSel = new Scanner(System.in);
        System.out.println("Choose which account to deposit to:\n"
                + "1. Chequings Account\n"
                + "2. Savings Account");
        int selection = accountSel.nextInt();
        if (selection == 1) {
            depositHelper1();
        } else if (selection == 2) {
            depositHelper2();
        } else {
            System.out.println("Please select a valid option.");
        }
    }

    // REQUIRES: depositAmt >= 0
    // MODIFIES: chequingsAccount balance and transaction list
    // EFFECTS: allows user to deposit an amount from the chequing account and the transaction gets added
    // to the transaction list of the Chequings account
    private void depositHelper1() {
        Scanner amount = new Scanner(System.in);
        System.out.println("Enter the amount you would like to deposit:");
        double depositAmt = amount.nextDouble();
        if (depositAmt >= 0) {
            chequingsAccount.deposit(depositAmt);
            Transaction depositTransaction = new Transaction("Deposit", depositAmt,
                    "User", "Chequings");
            chequingsAccount.addTransaction(depositTransaction);
            workRoom.addChequingsTransactionToWorkRoom(depositTransaction);
            System.out.println("Transaction Successful. \n"
                    + "Your current balance in your Chequings Account is: $" + chequingsAccount.getBalance());
        } else {
            System.out.println("You can't deposit a negative amount. Please try again.");
        }
    }

    // REQUIRES: depositAmt >= 0
    // MODIFIES: savingsAccount balance and transaction list
    // EFFECTS: allows user to deposit an amount from the savings account and the transaction gets added
    // to the transaction list of the Savings account
    private void depositHelper2() {
        Scanner amount = new Scanner(System.in);
        System.out.println("Enter the amount you would like to deposit:");
        double depositAmt = amount.nextDouble();
        if (depositAmt >= 0) {
            savingsAccount.deposit(depositAmt);
            Transaction depositTransaction = new Transaction("Deposit", depositAmt,
                    "User", "Savings");
            savingsAccount.addTransaction(depositTransaction);
            workRoom.addSavingsTransactionToWorkRoom(depositTransaction);
            System.out.println("Transaction Successful. \n"
                    + "Your current balance in your Savings Account is: $" + savingsAccount.getBalance());
        } else {
            System.out.println("You can't deposit a negative amount. Please try again.");
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Allows the user to transfer an amount from one Account to the other
    private void transferOperation() {
        Scanner accountSel = new Scanner(System.in);
        System.out.println("From which account do you want to transfer money from:\n"
                + "1. Chequings Account\n"
                + "2. Savings Account");
        int selection = accountSel.nextInt();
        if (selection == 1) {
            transferHelper1();
        } else if (selection == 2) {
            transferHelper2();
        } else {
            System.out.println("Please select a valid option.");
        }
    }

    // REQUIRES: transferAmount >= 0 && transferAmount <= the balance of Chequings account
    // MODIFIES: balance of both accounts and the transaction list of chequings account
    // EFFECTS: allows user to transfer an amount from one account to the other and the transaction
    // gets added to the transaction list of the Chequings account
    private void transferHelper1() {
        Scanner amount = new Scanner(System.in);
        System.out.println("Please enter the amount you wish to transfer:");
        double transferAmount = amount.nextDouble();
        if ((transferAmount >= 0) && (transferAmount <= chequingsAccount.getBalance())) {
            chequingsAccount.withdraw(transferAmount);
            savingsAccount.deposit(transferAmount);
            Transaction transferTransaction = new Transaction("Transfer", transferAmount,
                    "Chequings", "Savings");
            chequingsAccount.addTransaction(transferTransaction);
            workRoom.addChequingsTransactionToWorkRoom(transferTransaction);
            System.out.println("Transfer Successful.\n"
                    + "Your new Chequings Account balance is: $" + chequingsAccount.getBalance()
                    + "\nYour new Savings Account balance is: $" + savingsAccount.getBalance());
        } else if (transferAmount > chequingsAccount.getBalance()) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("You can't transfer a negative amount. Please try again.");
        }
    }

    // REQUIRES: transferAmount >= 0 && transferAmount <= the balance of Savings account
    // MODIFIES: balance of both accounts and the transaction list of savings account
    // EFFECTS: allows user to transfer an amount from one account to the other and the transaction
    // gets added to the transaction list of the Savings account
    private void transferHelper2() {
        Scanner amount = new Scanner(System.in);
        System.out.println("Please enter the amount you wish to transfer:");
        double transferAmount = amount.nextDouble();
        if ((transferAmount >= 0) && (transferAmount <= savingsAccount.getBalance())) {
            savingsAccount.withdraw(transferAmount);
            chequingsAccount.deposit(transferAmount);
            Transaction transferTransaction = new Transaction("Transfer", transferAmount,
                    "Savings", "Chequings");
            savingsAccount.addTransaction(transferTransaction);
            workRoom.addSavingsTransactionToWorkRoom(transferTransaction);
            System.out.println("Transfer Successful.\n"
                        + "Your new Savings Account balance is: $" + savingsAccount.getBalance()
                        + "\nYour new Chequings Account balance is: $" + chequingsAccount.getBalance());
        } else if (transferAmount > savingsAccount.getBalance()) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("You can't transfer a negative amount. Please try again.");
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Prints out the transaction lists from both the Chequings and Savings Accounts
    private void showTransactions() {
        System.out.println("Your Chequings Account transactions:");
        for (Transaction t : chequingTransactionsList) {
            System.out.println("Operation: " + t.getOperation() + " - Amount: "
                    + t.getTransAmount() + " - From: " + t.getTakenFrom()
                    + " - To: " + t.getSentTo());
        }
        System.out.println("\nYour Savings Account transactions:");
        for (Transaction t : savingTransactionsList) {
            System.out.println("Operation: " + t.getOperation() + " - Amount: "
                    + t.getTransAmount() + " - From: " + t.getTakenFrom()
                    + " - To: " + t.getSentTo());
        }
        System.out.println("");
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Saves the state of the atm of the workroom to file
    private void saveAtmWorkRoom() {
        workRoom.addChequingBalanceToWorkRoom(chequingsAccount.getBalance());
        workRoom.addSavingBalanceToWorkRoom(savingsAccount.getBalance());
        try {
            jsonWriter.open();
            jsonWriter.write(workRoom);
            jsonWriter.close();
            System.out.println(workRoom.getName() + "has been saved to: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to file: " + JSON_STORE);
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Loads the workroom into the ATM
    private void loadAtmWorkRoom() {
        try {
            workRoom = jsonReader.read();
            System.out.println(workRoom.getName() + "has been loaded from: " + JSON_STORE);
            chequingsAccount.withdraw(chequingsAccount.getBalance());
            chequingsAccount.deposit(workRoom.getChequingsBalance());
            savingsAccount.withdraw(savingsAccount.getBalance());
            savingsAccount.deposit(workRoom.getSavingsBalance());
            String jsonData = readFile(JSON_STORE);
            JSONObject jsonObject = new JSONObject(jsonData);
            addChequingTransactions(jsonObject);
            addSavingsTransactions(jsonObject);
        } catch (IOException e) {
            System.out.println("Unable to load from file: " + JSON_STORE);
        }
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
    // MODIFIES: Account
    // EFFECTS: Converts the chequings transactions from file to list of transaction associated to the account
    private void addChequingTransactions(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Chequings Account Transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransactionJson = (JSONObject) json;
            String operation = nextTransactionJson.getString("Operation");
            double transAmount = nextTransactionJson.getDouble("Transaction Amount");
            String takenFrom = nextTransactionJson.getString("Taken From");
            String sentTo = nextTransactionJson.getString("Sent to");
            Transaction nextTransaction = new Transaction(operation, transAmount, takenFrom, sentTo);
            chequingsAccount.addTransaction(nextTransaction);
        }
    }

    // REQUIRES: --
    // MODIFIES: Account
    // EFFECTS: Converts the savings transactions from file to list of transaction associated to the account
    private void addSavingsTransactions(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Savings Account Transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransactionJson = (JSONObject) json;
            String operation = nextTransactionJson.getString("Operation");
            double transAmount = nextTransactionJson.getDouble("Transaction Amount");
            String takenFrom = nextTransactionJson.getString("Taken From");
            String sentTo = nextTransactionJson.getString("Sent to");
            Transaction nextTransaction = new Transaction(operation, transAmount, takenFrom, sentTo);
            savingsAccount.addTransaction(nextTransaction);
        }
    }
}