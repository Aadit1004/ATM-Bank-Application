package ui;

import model.Account;
import model.AtmWorkRoom;
import model.Event;
import model.EventLog;
import model.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

//ATM GUI application
public class AtmGUI extends JFrame {

    private static final int accountID = 12345678;
    private static final int pin = 1234;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final String bankLogoURL = "./src/main/ui/banklogo.png";
    private ArrayList<Transaction> chequingTransactionsList = new ArrayList<>();
    private ArrayList<Transaction> savingTransactionsList = new ArrayList<>();
    private Account chequingsAccount = new Account(5000.00, "Chequing Account", chequingTransactionsList);
    private Account savingsAccount = new Account(1000.00, "Savings Account", savingTransactionsList);
    private static final String JSON_STORE = "./data/workroom.json";
    private AtmWorkRoom workRoom;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String[] accounts = {"Chequings", "Savings"};
    private String[] columnNames = {"Operation", "Transaction Amount", "Taken From", "Sent to"};

    //Login Page fields:
    private JTextField accountTextField = new JTextField();
    private JPasswordField pinTextField = new JPasswordField();
    private JButton b1 = new JButton("Login");
    private JLabel accountIDText = new JLabel("Account ID:");
    private JLabel pinText = new JLabel("PIN:");
    private ImageIcon logo = new ImageIcon(bankLogoURL);
    private JLabel bankLogo = new JLabel(logo);

    //Menu Page fields:
    private JButton b2 = new JButton("Withdraw");
    private JButton b3 = new JButton("Deposit");
    private JButton b4 = new JButton("Transfer");
    private JButton b5 = new JButton("Balance");
    private JButton b6 = new JButton("Save");
    private JButton b7 = new JButton("Load");
    private JButton b8 = new JButton("Transactions");
    private JButton b9 = new JButton("Quit");

    //Withdraw Page fields:
    private JLabel withdrawCheqBalLabel = new JLabel();
    private JLabel withdrawSavBalLabel = new JLabel();
    private JLabel withdrawQuestion = new JLabel("Which account would you like to withdraw from?");
    private JLabel withdrawAmountText = new JLabel("How much would you like to withdraw?");
    private JTextField withdrawAmountTextField = new JTextField();
    private JButton b10 = new JButton("Go Back");
    private JButton b11 = new JButton("Withdraw and add Transaction");
    private JComboBox<String> withdrawOptions = new JComboBox<>(accounts);

    //Deposit Page fields:
    private JLabel depositCheqBalLabel = new JLabel();
    private JLabel depositSavBalLabel = new JLabel();
    private JLabel depositQuestion = new JLabel("Which account would you like to deposit from?");
    private JLabel depositAmountText = new JLabel("How much would you like to deposit?");
    private JTextField depositAmountTextField = new JTextField();
    private JButton b12 = new JButton("Go Back");
    private JButton b13 = new JButton("Deposit and add Transaction");
    private JComboBox<String> depositOptions = new JComboBox<>(accounts);

    //Transfer Page fields:
    private JLabel transferCheqBalLabel = new JLabel();
    private JLabel transferSavBalLabel = new JLabel();
    private JLabel transferQuestion = new JLabel("Which account would you like to Transfer from?");
    private JLabel transferAmountText = new JLabel("How much would you like to Transfer?");
    private JTextField transferAmountTextField = new JTextField();
    private JButton b14 = new JButton("Go Back");
    private JButton b15 = new JButton("Transfer and add Transaction");
    private JComboBox<String> transferOptions = new JComboBox<>(accounts);

    //Transactions Page fields:
    private JLabel transactionsCheqLabel = new JLabel("Chequings Transactions:");
    private JLabel transactionsSavLabel = new JLabel("Savings Transactions:");
    private JButton b16 = new JButton("Go Back");
    private String[][] data1;
    private JTable table1;
    private JScrollPane scrollPane1;
    private String[][] data2;
    private JTable table2;
    private JScrollPane scrollPane2;

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: calls the login page to start
    public AtmGUI() {
        createLoginPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the window and login page
    private void createLoginPage() {
        JDesktopPane desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());

        setContentPane(desktop);
        setTitle("Aadit's ATM");
        setSize(WIDTH, HEIGHT);

        createLoginButton();
        createAccountIdField();
        createPinField();
        createLogo();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            AtmGUI.this.requestFocusInWindow();
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the login button for the login page
    private void createLoginButton() {
        b1.setVerticalTextPosition(AbstractButton.BOTTOM);
        b1.setHorizontalTextPosition(AbstractButton.CENTER);
        b1.setMnemonic(KeyEvent.VK_ENTER);
        add(b1);
        b1.setBackground(Color.GRAY);
        b1.setForeground(Color.WHITE);
        b1.setSize(100, 50);
        b1.setVisible(true);
        b1.setLocation(250, HEIGHT - 150);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: login button effects, checks if accoundID and pin is valid and asks if user would like
    // to load information
    private void loginButton(ActionEvent e) {
        String enteredId = String.valueOf(accountTextField.getText());
        String enteredPin = String.valueOf(pinTextField.getPassword());
        if (enteredId.equals(Integer.toString(accountID)) && enteredPin.equals(Integer.toString(pin))) {
            int result = JOptionPane.showConfirmDialog(this, "Would you like to load your information?",
                    "Load?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                hideLoginPage();
                createMenuPage();
                loadAtmWorkRoom();
                JOptionPane.showMessageDialog(null, "Information has been loaded!");
            } else {
                hideLoginPage();
                createMenuPage();
            }
        } else {
            JOptionPane.showMessageDialog(null, "       Incorrect ID or Pin!");
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: hides the elements of the login page
    private void hideLoginPage() {
        b1.setVisible(false);
        accountIDText.setVisible(false);
        accountTextField.setVisible(false);
        pinText.setVisible(false);
        pinTextField.setVisible(false);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the accoundID text and text field
    private void createAccountIdField() {
        add(accountIDText);
        accountIDText.setSize(100, 50);
        accountIDText.setVisible(true);
        accountIDText.setLocation(180, 250);
        add(accountTextField);
        accountTextField.setSize(100, 25);
        accountTextField.setVisible(true);
        accountTextField.setLocation(280, 265);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the pin text and text field
    private void createPinField() {
        add(pinText);
        pinText.setSize(100, 50);
        pinText.setVisible(true);
        pinText.setLocation(190, 300);
        add(pinTextField);
        pinTextField.setSize(100, 25);
        pinTextField.setVisible(true);
        pinTextField.setLocation(280, 315);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: adds the bank logo on the window
    private void createLogo() {
        add(bankLogo);
        bankLogo.setSize(500, 200);
        bankLogo.setLocation(50, 20);
        bankLogo.setVisible(true);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the elements of the menu page and initiates the workroom
    private void createMenuPage() {
        workRoom = new AtmWorkRoom("ATM WorkRoom");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        createWithdrawButton();
        createDepositButton();
        createTransferButton();
        createCheckBalanceButton();
        createSaveButton();
        createLoadButton();
        createTransactionsButton();
        createQuitButton();
        createPages();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the withdraw, deposit, transfer, and transactions pages but all set false for visible
    private void createPages() {
        createWithdrawPage();
        createDepositPage();
        createTransferPage();
        createTransactionsPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the withdraw button on the menu
    private void createWithdrawButton() {
        b2.setVerticalTextPosition(AbstractButton.BOTTOM);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b2);
        b2.setBackground(Color.DARK_GRAY);
        b2.setForeground(Color.WHITE);
        b2.setSize(100, 50);
        b2.setLocation(90, 270);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: withdraw button operation, hides menu page and shows withdraw page
    private void withdrawButton(ActionEvent e) {
        hideMenuPage();
        showWithdrawPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the deposit button on the menu
    private void createDepositButton() {
        b3.setVerticalTextPosition(AbstractButton.BOTTOM);
        b3.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b3);
        b3.setBackground(Color.DARK_GRAY);
        b3.setForeground(Color.WHITE);
        b3.setSize(100, 50);
        b3.setLocation(240, 270);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: deposit button operation, hides menu page and shows deposit page
    private void depositButton(ActionEvent e) {
        hideMenuPage();
        showDepositPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the transfer button on the menu
    private void createTransferButton() {
        b4.setVerticalTextPosition(AbstractButton.BOTTOM);
        b4.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b4);
        b4.setBackground(Color.DARK_GRAY);
        b4.setForeground(Color.WHITE);
        b4.setSize(100, 50);
        b4.setLocation(390, 270);
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: transfer button operation, hides menu page and shows transfer page
    private void transferButton(ActionEvent e) {
        hideMenuPage();
        showTransferPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the check balance button on menu
    private void createCheckBalanceButton() {
        b5.setVerticalTextPosition(AbstractButton.BOTTOM);
        b5.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b5);
        b5.setBackground(Color.DARK_GRAY);
        b5.setForeground(Color.WHITE);
        b5.setSize(100, 50);
        b5.setLocation(90, 360);
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalanceButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: check balance button operation, shows balance of chequings and savings account as a pop-up message
    private void checkBalanceButton(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Your Chequings Account Balance is $" + chequingsAccount.getBalance()
                + "\n Your Savings Account Balance is $" + savingsAccount.getBalance());
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the save button on menu
    private void createSaveButton() {
        b6.setVerticalTextPosition(AbstractButton.BOTTOM);
        b6.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b6);
        b6.setBackground(Color.DARK_GRAY);
        b6.setForeground(Color.WHITE);
        b6.setSize(100, 50);
        b6.setLocation(240, 360);
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: save button operation, saves data to workroom, confirmation shows up as pop-up message
    private void saveButton(ActionEvent e) {
        saveAtmWorkRoom();
        JOptionPane.showMessageDialog(null, "Information has been saved!");
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the load button on menu
    private void createLoadButton() {
        b7.setVerticalTextPosition(AbstractButton.BOTTOM);
        b7.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b7);
        b7.setBackground(Color.DARK_GRAY);
        b7.setForeground(Color.WHITE);
        b7.setSize(100, 50);
        b7.setLocation(390, 360);
        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: load button operation, loads information from workroom, confirmations shows up as pop-up message
    private void loadButton(ActionEvent e) {
        loadAtmWorkRoom();
        JOptionPane.showMessageDialog(null, "Information has been loaded!");
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates transactions button on menu
    private void createTransactionsButton() {
        b8.setVerticalTextPosition(AbstractButton.BOTTOM);
        b8.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b8);
        b8.setBackground(Color.DARK_GRAY);
        b8.setForeground(Color.WHITE);
        b8.setSize(125, 50);
        b8.setLocation(150, 450);
        b8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionsButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: transactions button operation, hides menu page and shows transactions page
    private void transactionsButton(ActionEvent e) {
        hideMenuPage();
        showTransactionsPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates quit button on menu
    private void createQuitButton() {
        b9.setVerticalTextPosition(AbstractButton.BOTTOM);
        b9.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b9);
        b9.setBackground(Color.DARK_GRAY);
        b9.setForeground(Color.WHITE);
        b9.setSize(100, 50);
        b9.setLocation(325, 450);
        b9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: quit button operation, quits the atm and asks the options if you would like to save information
    private void quitButton(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(this, "Would you like to save your information?",
                "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            saveAtmWorkRoom();
            JOptionPane.showMessageDialog(null, "Information has been saved!");
        }
        JOptionPane.showMessageDialog(null, "Thanks for using the ATM!");
        this.setVisible(false);
        printLog();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Prints log to console once ATM is closed
    private void printLog() {
        System.out.println("Event Log:");
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString() + "\n");
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: hides all the elements of the menu page
    private void hideMenuPage() {
        b2.setVisible(false);
        b3.setVisible(false);
        b4.setVisible(false);
        b5.setVisible(false);
        b6.setVisible(false);
        b7.setVisible(false);
        b8.setVisible(false);
        b9.setVisible(false);
        bankLogo.setVisible(false);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: shows all the elements of the menu page
    private void showMenuPage() {
        b2.setVisible(true);
        b3.setVisible(true);
        b4.setVisible(true);
        b5.setVisible(true);
        b6.setVisible(true);
        b7.setVisible(true);
        b8.setVisible(true);
        b9.setVisible(true);
        bankLogo.setVisible(true);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates all the elements of the withdraw page but hides them
    private void createWithdrawPage() {
        createWithdrawLabelsAndComboBox();
        createWithdrawGoBackButton();
        createCompleteWithdrawButton();
        hideWithdrawPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the labels and combo box for withdraw page
    private void createWithdrawLabelsAndComboBox() {
        add(withdrawCheqBalLabel);
        withdrawCheqBalLabel.setSize(300, 20);
        withdrawCheqBalLabel.setLocation(0, 25);
        add(withdrawSavBalLabel);
        withdrawSavBalLabel.setSize(300, 50);
        withdrawSavBalLabel.setLocation(0, 35);
        add(withdrawQuestion);
        withdrawQuestion.setSize(300, 15);
        withdrawQuestion.setLocation(150, HEIGHT / 4);
        add(withdrawOptions);
        withdrawOptions.setSize(115, 20);
        withdrawOptions.setLocation(225, HEIGHT / 3);
        add(withdrawAmountText);
        withdrawAmountText.setSize(250, 25);
        withdrawAmountText.setLocation(60, HEIGHT - 300);
        add(withdrawAmountTextField);
        withdrawAmountTextField.setSize(150, 25);
        withdrawAmountTextField.setLocation(300, HEIGHT - 300);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates go back buttonm on withdraw page
    private void createWithdrawGoBackButton() {
        b10.setVerticalTextPosition(AbstractButton.BOTTOM);
        b10.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b10);
        b10.setBackground(Color.DARK_GRAY);
        b10.setForeground(Color.WHITE);
        b10.setSize(100, 50);
        b10.setLocation(100, HEIGHT - 150);
        b10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawGoBackButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: withdraw go back button operation, hides withdraw page and shows menu page
    private void withdrawGoBackButton(ActionEvent e) {
        hideWithdrawPage();
        showMenuPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates complete withdraw button
    private void createCompleteWithdrawButton() {
        b11.setVerticalTextPosition(AbstractButton.BOTTOM);
        b11.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b11);
        b11.setBackground(Color.DARK_GRAY);
        b11.setForeground(Color.WHITE);
        b11.setSize(250, 50);
        b11.setLocation(300, HEIGHT - 150);
        b11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeWithdrawButton1(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: checks if able to complete valid transaction then hides withdraw page and shows menu page, else
    // has another try to complete valid transaction
    private void completeWithdrawButton1(ActionEvent e) {
        String enteredWithdrawAmount = String.valueOf(withdrawAmountTextField.getText());
        Double amount = Double.valueOf(enteredWithdrawAmount);
        String accountSelection = String.valueOf(withdrawOptions.getSelectedItem());
        if (withdrawOperation(amount, accountSelection)) {
            JOptionPane.showMessageDialog(null, "Withdraw Successful!");
            hideWithdrawPage();
            showMenuPage();
        }

    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Allows the user to withdraw an amount from either Account
    private boolean withdrawOperation(Double amount, String account) {
        if (account.equals("Chequings")) {
            return withdrawHelper1(amount);
        }
        return withdrawHelper2(amount);
    }

    // REQUIRES: withdrawAmt >= 0 && withdrawAmt <= balance of Chequings Account
    // MODIFIES: chequingsAccount balance and transaction list
    // EFFECTS: allows user to withdraw an amount from the chequings account and the transaction gets added
    // to the transaction list of the Chequings account
    private boolean withdrawHelper1(Double withdrawAmt) {
        if ((withdrawAmt >= 0) && (withdrawAmt <= chequingsAccount.getBalance())) {
            chequingsAccount.withdraw(withdrawAmt);
            Transaction withdrawTransaction = new Transaction("Withdraw", withdrawAmt,
                    "Chequings", "User");
            chequingsAccount.addTransaction(withdrawTransaction);
            workRoom.addChequingsTransactionToWorkRoom(withdrawTransaction);
            return true;
        } else if ((withdrawAmt > chequingsAccount.getBalance())) {
            JOptionPane.showMessageDialog(null, "Insufficient Funds!");
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount. Please try again.");
            return false;
        }
    }

    // REQUIRES: withdrawAmt >= 0 && withdrawAmt <= balance of Savings Account
    // MODIFIES: savingsAccount balance and transaction list
    // EFFECTS: allows user to withdraw an amount from the savings account and the transaction gets added
    // to the transaction list of the Savings account
    private boolean withdrawHelper2(Double withdrawAmt) {
        if ((withdrawAmt >= 0) && (withdrawAmt <= savingsAccount.getBalance())) {
            savingsAccount.withdraw(withdrawAmt);
            Transaction withdrawTransaction = new Transaction("Withdraw", withdrawAmt,
                    "Savings", "User");
            savingsAccount.addTransaction(withdrawTransaction);
            workRoom.addSavingsTransactionToWorkRoom(withdrawTransaction);
            return true;
        } else if ((withdrawAmt > savingsAccount.getBalance())) {
            JOptionPane.showMessageDialog(null, "Insufficient Funds!");
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount. Please try again.");
            return false;
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: hides elements of withdraw page
    private void hideWithdrawPage() {
        withdrawAmountText.setVisible(false);
        withdrawAmountTextField.setVisible(false);
        withdrawQuestion.setVisible(false);
        withdrawCheqBalLabel.setVisible(false);
        withdrawSavBalLabel.setVisible(false);
        b10.setVisible(false);
        b11.setVisible(false);
        withdrawOptions.setVisible(false);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: shows elements of withdraw page
    private void showWithdrawPage() {
        withdrawCheqBalLabel.setText("Your current available Chequings Balance is $"
                + chequingsAccount.getBalance());
        withdrawSavBalLabel.setText("Your current available Savings Balance is $"
                + savingsAccount.getBalance());
        withdrawAmountText.setVisible(true);
        withdrawAmountTextField.setVisible(true);
        withdrawQuestion.setVisible(true);
        withdrawCheqBalLabel.setVisible(true);
        withdrawSavBalLabel.setVisible(true);
        b10.setVisible(true);
        b11.setVisible(true);
        withdrawOptions.setVisible(true);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates elements of deposit page and hides them
    private void createDepositPage() {
        createDepositLabelsAndComboBox();
        createDepositGoBackButton();
        createCompleteDepositButton();
        hideDepositPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: hide all elements of deposit page
    private void hideDepositPage() {
        depositAmountText.setVisible(false);
        depositAmountTextField.setVisible(false);
        depositQuestion.setVisible(false);
        depositSavBalLabel.setVisible(false);
        depositCheqBalLabel.setVisible(false);
        b12.setVisible(false);
        b13.setVisible(false);
        depositOptions.setVisible(false);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: shows all elements of deposit page
    private void showDepositPage() {
        depositCheqBalLabel.setText("Your current available Chequings Balance is $"
                + chequingsAccount.getBalance());
        depositAmountText.setVisible(true);
        depositAmountTextField.setVisible(true);
        depositQuestion.setVisible(true);
        depositSavBalLabel.setText("Your current available Savings Balance is $"
                + savingsAccount.getBalance());
        depositSavBalLabel.setVisible(true);
        depositCheqBalLabel.setVisible(true);
        b12.setVisible(true);
        b13.setVisible(true);
        depositOptions.setVisible(true);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates labels and combo box for deposit page
    private void createDepositLabelsAndComboBox() {
        add(depositCheqBalLabel);
        depositCheqBalLabel.setSize(300, 15);
        depositCheqBalLabel.setLocation(0, 25);
        add(depositSavBalLabel);
        depositSavBalLabel.setSize(300, 50);
        depositSavBalLabel.setLocation(0, 35);
        add(depositQuestion);
        depositQuestion.setSize(300, 15);
        depositQuestion.setLocation(150, HEIGHT / 4);
        add(depositOptions);
        depositOptions.setSize(115, 20);
        depositOptions.setLocation(225, HEIGHT / 3);
        add(depositAmountText);
        depositAmountText.setSize(250, 25);
        depositAmountText.setLocation(60, HEIGHT - 300);
        add(depositAmountTextField);
        depositAmountTextField.setSize(150, 25);
        depositAmountTextField.setLocation(300, HEIGHT - 300);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates go back button for deposit page
    private void createDepositGoBackButton() {
        b12.setVerticalTextPosition(AbstractButton.BOTTOM);
        b12.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b12);
        b12.setBackground(Color.DARK_GRAY);
        b12.setForeground(Color.WHITE);
        b12.setSize(100, 50);
        b12.setLocation(100, HEIGHT - 150);
        b12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositGoBackButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: go back deposit button, hides deposit page and shows menu page
    private void depositGoBackButton(ActionEvent e) {
        hideDepositPage();
        showMenuPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates complete deposit button on deposit page
    private void createCompleteDepositButton() {
        b13.setVerticalTextPosition(AbstractButton.BOTTOM);
        b13.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b13);
        b13.setBackground(Color.DARK_GRAY);
        b13.setForeground(Color.WHITE);
        b13.setSize(250, 50);
        b13.setLocation(300, HEIGHT - 150);
        b13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeDepositButton1(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: completes deposit transaction, if valid closed deposit page and shows menu page
    // else has another try to complete valid transaction
    private void completeDepositButton1(ActionEvent e) {
        String enteredDepositAmount = String.valueOf(depositAmountTextField.getText());
        Double amount = Double.valueOf(enteredDepositAmount);
        String accountSelection = String.valueOf(depositOptions.getSelectedItem());
        if (depositOperation(amount, accountSelection)) {
            JOptionPane.showMessageDialog(null, "Deposit Successful!");
            hideDepositPage();
            showMenuPage();
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Allows the user to deposit an amount from either Account
    private boolean depositOperation(Double amount, String account) {
        if (account.equals("Chequings")) {
            return depositHelper1(amount);
        }
        return depositHelper2(amount);
    }

    // REQUIRES: depositAmt >= 0
    // MODIFIES: chequingsAccount balance and transaction list
    // EFFECTS: allows user to deposit an amount from the chequing account and the transaction gets added
    // to the transaction list of the Chequings account
    private boolean depositHelper1(Double depositAmt) {
        if (depositAmt >= 0) {
            chequingsAccount.deposit(depositAmt);
            Transaction depositTransaction = new Transaction("Deposit", depositAmt,
                    "User", "Chequings");
            chequingsAccount.addTransaction(depositTransaction);
            workRoom.addChequingsTransactionToWorkRoom(depositTransaction);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount. Please try again.");
            return false;
        }
    }

    // REQUIRES: depositAmt >= 0
    // MODIFIES: savingsAccount balance and transaction list
    // EFFECTS: allows user to deposit an amount from the savings account and the transaction gets added
    // to the transaction list of the Savings account
    private boolean depositHelper2(Double depositAmt) {
        if (depositAmt >= 0) {
            savingsAccount.deposit(depositAmt);
            Transaction depositTransaction = new Transaction("Deposit", depositAmt,
                    "User", "Savings");
            savingsAccount.addTransaction(depositTransaction);
            workRoom.addSavingsTransactionToWorkRoom(depositTransaction);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount. Please try again.");
            return false;
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates elements of transfer page and hides them
    private void createTransferPage() {
        createTransferLabelsAndComboBox();
        createTransferGoBackButton();
        createCompleteTransferButton();
        hideTransferPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: hides the elements of the transfer page
    private void hideTransferPage() {
        transferCheqBalLabel.setVisible(false);
        transferSavBalLabel.setVisible(false);
        transferOptions.setVisible(false);
        transferQuestion.setVisible(false);
        transferAmountText.setVisible(false);
        transferAmountTextField.setVisible(false);
        b14.setVisible(false);
        b15.setVisible(false);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: shows the elements of the transfer page
    private void showTransferPage() {
        transferCheqBalLabel.setText("Your current available Chequings Balance is $"
                + chequingsAccount.getBalance());
        transferSavBalLabel.setText("Your current available Savings Balance is $"
                + savingsAccount.getBalance());
        transferCheqBalLabel.setVisible(true);
        transferSavBalLabel.setVisible(true);
        transferOptions.setVisible(true);
        transferQuestion.setVisible(true);
        transferAmountText.setVisible(true);
        transferAmountTextField.setVisible(true);
        b14.setVisible(true);
        b15.setVisible(true);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the labels and combo box for transfer page
    private void createTransferLabelsAndComboBox() {
        add(transferCheqBalLabel);
        transferCheqBalLabel.setSize(300, 15);
        transferCheqBalLabel.setLocation(0, 25);
        add(transferSavBalLabel);
        transferSavBalLabel.setSize(300, 50);
        transferSavBalLabel.setLocation(0, 35);
        add(transferQuestion);
        transferQuestion.setSize(300, 15);
        transferQuestion.setLocation(150, HEIGHT / 4);
        add(transferAmountText);
        transferAmountText.setSize(250, 25);
        transferAmountText.setLocation(60, HEIGHT - 300);
        add(transferAmountTextField);
        transferAmountTextField.setSize(150, 25);
        transferAmountTextField.setLocation(300, HEIGHT - 300);
        add(transferOptions);
        transferOptions.setSize(115, 20);
        transferOptions.setLocation(225, HEIGHT / 3);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the go back button for transfer page
    private void createTransferGoBackButton() {
        b14.setVerticalTextPosition(AbstractButton.BOTTOM);
        b14.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b14);
        b14.setBackground(Color.DARK_GRAY);
        b14.setForeground(Color.WHITE);
        b14.setSize(100, 50);
        b14.setLocation(100, HEIGHT - 150);
        b14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferGoBackButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: transfer go back button operation, hides transfer page and shows menu page
    private void transferGoBackButton(ActionEvent e) {
        hideTransferPage();
        showMenuPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates complete transfer button
    private void createCompleteTransferButton() {
        b15.setVerticalTextPosition(AbstractButton.BOTTOM);
        b15.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b15);
        b15.setBackground(Color.DARK_GRAY);
        b15.setForeground(Color.WHITE);
        b15.setSize(250, 50);
        b15.setLocation(300, HEIGHT - 150);
        b15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeTransferButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: completes valid transfer transaction and hides transfer page and shows menu page
    // else gives another try to complete a valid transaction
    private void completeTransferButton(ActionEvent e) {
        String enteredTransferAmount = String.valueOf(transferAmountTextField.getText());
        Double amount = Double.valueOf(enteredTransferAmount);
        String accountSelection = String.valueOf(transferOptions.getSelectedItem());
        if (transferOperation(amount, accountSelection)) {
            JOptionPane.showMessageDialog(null, "Transfer Successful!");
            hideTransferPage();
            showMenuPage();
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Allows the user to transfer an amount from one Account to the other
    private boolean transferOperation(Double amount, String account) {
        if (account.equals("Chequings")) {
            return transferHelper1(amount);
        }
        return transferHelper2(amount);
    }

    // REQUIRES: transferAmount >= 0 && transferAmount <= the balance of Chequings account
    // MODIFIES: balance of both accounts and the transaction list of chequings account
    // EFFECTS: allows user to transfer an amount from one account to the other and the transaction
    // gets added to the transaction list of the Chequings account
    private boolean transferHelper1(Double transferAmount) {
        if ((transferAmount >= 0) && (transferAmount <= chequingsAccount.getBalance())) {
            chequingsAccount.withdraw(transferAmount);
            savingsAccount.deposit(transferAmount);
            Transaction transferTransaction = new Transaction("Transfer", transferAmount,
                    "Chequings", "Savings");
            chequingsAccount.addTransaction(transferTransaction);
            workRoom.addChequingsTransactionToWorkRoom(transferTransaction);
            return true;
        } else if (transferAmount > chequingsAccount.getBalance()) {
            JOptionPane.showMessageDialog(null, "Insufficient Funds!");
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount. Please try again.");
            return false;
        }
    }

    // REQUIRES: transferAmount >= 0 && transferAmount <= the balance of Savings account
    // MODIFIES: balance of both accounts and the transaction list of savings account
    // EFFECTS: allows user to transfer an amount from one account to the other and the transaction
    // gets added to the transaction list of the Savings account
    private boolean transferHelper2(Double transferAmount) {
        if ((transferAmount >= 0) && (transferAmount <= savingsAccount.getBalance())) {
            savingsAccount.withdraw(transferAmount);
            chequingsAccount.deposit(transferAmount);
            Transaction transferTransaction = new Transaction("Transfer", transferAmount,
                    "Savings", "Chequings");
            savingsAccount.addTransaction(transferTransaction);
            workRoom.addSavingsTransactionToWorkRoom(transferTransaction);
            return true;
        } else if (transferAmount > savingsAccount.getBalance()) {
            JOptionPane.showMessageDialog(null, "Insufficient Funds!");
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount. Please try again.");
            return false;
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates elements of transaction page and hides them
    private void createTransactionsPage() {
        createChequingTransactionsTable();
        createSavingTransactionsTable();
        createTransactionsLabel();
        createTransactionsGoBackButton();
        hideTransactionsPage();
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: hides the elements of transactions page
    private void hideTransactionsPage() {
        transactionsCheqLabel.setVisible(false);
        transactionsSavLabel.setVisible(false);
        b16.setVisible(false);
        scrollPane1.setVisible(false);
        scrollPane2.setVisible(false);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: shows the elements of the transactions page
    private void showTransactionsPage() {
        addCheqTransactionsToData();
        table1.setFillsViewportHeight(true);
        scrollPane1.setVisible(true);
        addSavTransactionsToData();
        table2.setFillsViewportHeight(true);
        scrollPane2.setVisible(true);
        transactionsCheqLabel.setVisible(true);
        transactionsSavLabel.setVisible(true);
        b16.setVisible(true);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: adds the chequings transactions list to data for table
    private void addCheqTransactionsToData() {
        for (int i = 0; i < chequingTransactionsList.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (chequingTransactionsList.get(i) != null) {
                    if (j == 0) {
                        data1[i][j] = chequingTransactionsList.get(i).getOperation();
                    } else if (j == 1) {
                        data1[i][j] = String.valueOf(chequingTransactionsList.get(i).getTransAmount());
                    } else if (j == 2) {
                        data1[i][j] = chequingTransactionsList.get(i).getTakenFrom();
                    } else {
                        data1[i][j] = chequingTransactionsList.get(i).getSentTo();
                    }
                }
            }
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: adds the savings transactions list to data for table
    private void addSavTransactionsToData() {
        for (int i = 0; i < savingTransactionsList.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (savingTransactionsList.get(i) != null) {
                    if (j == 0) {
                        data2[i][j] = savingTransactionsList.get(i).getOperation();
                    } else if (j == 1) {
                        data2[i][j] = String.valueOf(savingTransactionsList.get(i).getTransAmount());
                    } else if (j == 2) {
                        data2[i][j] = savingTransactionsList.get(i).getTakenFrom();
                    } else {
                        data2[i][j] = savingTransactionsList.get(i).getSentTo();
                    }
                }
            }
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the table for chequings account transactions
    private void createChequingTransactionsTable() {
        int rows = 21;
        int columns = 4;
        data1 = new String[rows][columns];
        for (int c = 0; c < data1.length; c++) {
            for (int r = 0; r < data1[c].length; r++) {
                data1[c][r] = "";
            }
        }
        table1 = new JTable(data1, columnNames);
        scrollPane1 = new JScrollPane(table1);
        add(scrollPane1);
        scrollPane1.setLocation(0, 15);
        scrollPane1.setSize(WIDTH, HEIGHT - 415);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates the table for savings account transactions
    private void createSavingTransactionsTable() {
        int rows = 21;
        int columns = 4;
        data2 = new String[rows][columns];
        for (int c = 0; c < data2.length; c++) {
            for (int r = 0; r < data2[c].length; r++) {
                data2[c][r] = "";
            }
        }
        table2 = new JTable(data2, columnNames);
        scrollPane2 = new JScrollPane(table2);
        add(scrollPane2);
        scrollPane2.setLocation(0, 215);
        scrollPane2.setSize(WIDTH, HEIGHT - 415);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates transactions labels for transactions page
    private void createTransactionsLabel() {
        add(transactionsCheqLabel);
        transactionsCheqLabel.setSize(200, 15);
        transactionsCheqLabel.setLocation(0, 0);
        add(transactionsSavLabel);
        transactionsSavLabel.setSize(200, 15);
        transactionsSavLabel.setLocation(0, 200);
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: creates transactions go back button
    private void createTransactionsGoBackButton() {
        b16.setVerticalTextPosition(AbstractButton.BOTTOM);
        b16.setHorizontalTextPosition(AbstractButton.CENTER);
        add(b16);
        b16.setBackground(Color.DARK_GRAY);
        b16.setForeground(Color.WHITE);
        b16.setSize(100, 50);
        b16.setLocation(250, HEIGHT - 150);
        b16.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionGoBackButton(e);
            }
        });
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: transactions go back button operation, hides transactions page and shows menu page
    private void transactionGoBackButton(ActionEvent e) {
        hideTransactionsPage();
        showMenuPage();
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
            //System.out.println(workRoom.getName() + "has been saved to: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            //System.out.println("Unable to save to file: " + JSON_STORE);
            JOptionPane.showMessageDialog(null, "Unable to save to file: " + JSON_STORE);
        }
    }

    // REQUIRES: --
    // MODIFIES: --
    // EFFECTS: Loads the workroom into the ATM
    private void loadAtmWorkRoom() {
        try {
            workRoom = jsonReader.read();
            //System.out.println(workRoom.getName() + "has been loaded from: " + JSON_STORE);
            chequingsAccount.withdraw(chequingsAccount.getBalance());
            chequingsAccount.deposit(workRoom.getChequingsBalance());
            savingsAccount.withdraw(savingsAccount.getBalance());
            savingsAccount.deposit(workRoom.getSavingsBalance());
            String jsonData = readFile(JSON_STORE);
            JSONObject jsonObject = new JSONObject(jsonData);
            addChequingTransactions(jsonObject);
            addSavingsTransactions(jsonObject);
        } catch (IOException e) {
            //System.out.println("Unable to load from file: " + JSON_STORE);
            JOptionPane.showMessageDialog(null, "Unable to load from file: " + JSON_STORE);
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