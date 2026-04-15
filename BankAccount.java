package banking.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount {

    private String accountNumber;
    private String ownerName;
    private double balance;
    private List<String> transactions; // We must store Transaction objects, not plain text.

    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactions() { //Method must return the correct type .SO instead of string it should be transaction
        return transactions;
    }

    protected void deductFromBalance(double amount) {
        balance -= amount;
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        balance += amount;
        transactions.add("Deposit: R" + amount + " | Balance: R" + balance);//Must create a Transaction object instead of a String.
    }

    public void printStatement() {
        System.out.println("Last Transactions:");
        int start = Math.max(0, transactions.size() - 5);
        for (int i = start; i < transactions.size(); i++) {
            System.out.println(transactions.get(i));
        }
    }
}
