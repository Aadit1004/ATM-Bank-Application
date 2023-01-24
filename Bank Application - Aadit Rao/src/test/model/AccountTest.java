package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account testBankAccount1;
    private ArrayList<Transaction> testList1;
    private Transaction testTransaction1;

    @BeforeEach
    void runBefore(){
        this.testList1 = new ArrayList<Transaction>();
        this.testTransaction1 = new Transaction("Withdraw", 100.00,
                "Savings", "User");
        this.testBankAccount1 = new Account(100.00, "test1", testList1);
    }

    @Test
    void testAccountNormal(){
    assertEquals(100, testBankAccount1.getBalance());
    assertEquals("test1", testBankAccount1.getName());
    assertTrue(testList1.isEmpty());
    }

    @Test
    void testAccountNoMoney(){
        ArrayList<Transaction> testList2 = new ArrayList<Transaction>();
        Account testBankAccount2 = new Account(0, "test2", testList2);
        assertEquals(0, testBankAccount2.getBalance());
        assertEquals("test2", testBankAccount2.getName());
        assertTrue(testList2.isEmpty());
    }

    @Test
    void testAccountNegative(){
        ArrayList<Transaction> testList3 = new ArrayList<Transaction>();
        Account testBankAccount3 = new Account(-100, "test3", testList3);
        assertEquals(0, testBankAccount3.getBalance());
        assertEquals("test3", testBankAccount3.getName());
        assertTrue(testList3.isEmpty());
    }

    @Test
    void testWithdrawOnce(){
        testBankAccount1.withdraw(75);
        assertEquals(25, testBankAccount1.getBalance());
    }

    @Test
    void testWithdrawMultiple(){
        testBankAccount1.withdraw(55);
        testBankAccount1.withdraw(12);
        assertEquals(33, testBankAccount1.getBalance());
    }

    @Test
    void testWithdrawNegative() {
        testBankAccount1.withdraw(-75);
        assertEquals(100, testBankAccount1.getBalance());
    }
    @Test
    void testDepositOnce(){
        testBankAccount1.deposit(10);
        assertEquals(110, testBankAccount1.getBalance());
    }

    @Test
    void testDepositMultiple(){
        testBankAccount1.deposit(55);
        testBankAccount1.deposit(1200);
        assertEquals(1355, testBankAccount1.getBalance());
    }

    @Test
    void testDepositNegative() {
        testBankAccount1.deposit(-100);
        assertEquals(100, testBankAccount1.getBalance());
    }

    @Test
    void testAddTransactionOnce() {
        testList1.add(testTransaction1);
        assertEquals(1, testList1.size());
    }

    @Test
    void testAddTransactionMultiple() {
        Transaction testTransaction2 = new Transaction("Deposit", 750.00,
                "User", "Chequings");
        testBankAccount1.addTransaction(testTransaction1);
        testBankAccount1.addTransaction(testTransaction2);
        assertEquals(2, testList1.size());
    }
}