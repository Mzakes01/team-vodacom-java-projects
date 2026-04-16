/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


/**
 *
 * @author letha
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//  Custom Exception
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

//  Transaction record
class Transaction {
    private final String type;
    private final double amount;
    private final double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return String.format("  %-10s  Amount: R%10.2f   Balance after: R%10.2f",
                type, amount, balanceAfter);
    }
}

//  Account
class Account {
    private double balance;
    private final List<Transaction> history = new ArrayList<>();

    public Account(double initialBalance) {
        this.balance = initialBalance;
        history.add(new Transaction("OPEN", initialBalance, initialBalance));
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Requested: R%.2f  |  Available: R%.2f",
                            amount, balance));
        }
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, balance));
    }

    public List<Transaction> getHistory() {
        return history;
    }
}

//  User hierarchy
abstract class User {
    protected final String username;
    protected final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public abstract String getRole();
}

class Customer extends User {
    private final Account account;

    public Customer(String username, String password, double initialBalance) {
        super(username, password);
        this.account = new Account(initialBalance);
    }

    public Account getAccount() { return account; }

    @Override
    public String getRole() { return "CUSTOMER"; }
}

class Teller extends User {
    public Teller(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() { return "TELLER"; }
}

public class Main {
    
    // ── Hardcoded users ──────────────────────
    private static final Map<String, User> USERS = new HashMap<>();

    static {
        USERS.put("alice",   new Customer("alice",   "alice123",  5000.00));
        USERS.put("bob",     new Customer("bob",     "bob456",    1200.50));
        USERS.put("teller1", new Teller  ("teller1", "teller999"));
    }

    private static final Scanner scanner = new Scanner(System.in);

    //  Entry point — outer login loop
    public static void main(String[] args) {
        while (true) {
            User user = loginScreen();
            if (user == null) continue;               // failed login — re-prompt
            if (user.getRole().equals("CUSTOMER")) {
                customerMenu((Customer) user);
            } else {
                tellerMenu((Teller) user);
            }
            // after logout we loop back to loginScreen automatically
        }
    }

    //  Login screen
    //  Returns authenticated User or null
    private static User loginScreen() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("  Please log in to continue");
        System.out.println("─────────────────────────────────────────");
        System.out.print("  Username : ");
        String username = scanner.nextLine().trim();
        System.out.print("  Password : ");
        String password = scanner.nextLine().trim();
        User user = USERS.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            System.out.println();
            System.out.println("  Invalid username or password. Please try again.");
            System.out.println();
            return null;
        }

        System.out.println();
        System.out.printf("  Welcome, %s!  (Role: %s)%n", user.getUsername(), user.getRole());
        System.out.println();
        return user;
    }

    //  Customer menu loop
    private static void customerMenu(Customer customer) {
        boolean loggedIn = true;

        while (loggedIn) {
            printCustomerMenu(customer.getUsername());

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewBalance(customer);
                case "2" -> deposit(customer);
                case "3" -> withdraw(customer);
                case "4" -> transactionHistory(customer);
                case "5" -> {
                    System.out.println();
                    System.out.println("  Logged out successfully. Goodbye, " + customer.getUsername() + "!");
                    System.out.println();
                    loggedIn = false;
                }
                default -> {
                    System.out.println();
                    System.out.println(" Invalid option. Please enter a number between 1 and 5.");
                    System.out.println();
                }
            }
        }
    }

    private static void printCustomerMenu(String username) {
        System.out.println("==============================================");
        System.out.printf( "  Customer Menu  ",
                username, " ".repeat(Math.max(0, 22 - username.length())));
        System.out.println("==============================================");
        System.out.println("  [1]  View Balance                       ");
        System.out.println("  [2]  Deposit                            ");
        System.out.println("  [3]  Withdraw                           ");
        System.out.println("  [4]  Transaction History                ");
        System.out.println("  [5]  Logout                             ");
        System.out.println("==============================================");
        System.out.print("  Enter choice: ");
    }

    //  Teller menu loop
    private static void tellerMenu(Teller teller) {
        boolean loggedIn = true;

        while (loggedIn) {
            printTellerMenu(teller.getUsername());

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> tellerViewAccount();
                case "2" -> tellerDeposit();
                case "3" -> tellerWithdraw();
                case "4" -> {
                    System.out.println();
                    System.out.println("  Logged out successfully. Goodbye, " + teller.getUsername() + "!");
                    System.out.println();
                    loggedIn = false;
                }
                default -> {
                    System.out.println();
                    System.out.println("  Invalid option. Please enter a number between 1 and 4.");
                    System.out.println();
                }
            }
        }
    }

    private static void printTellerMenu(String username) {
        System.out.println("===============================================");
        System.out.printf( " Teller Menu  ",
                username, " ".repeat(Math.max(0, 24 - username.length())));
        System.out.println("===============================================");
        System.out.println("  [1]  View Customer Account              ");
        System.out.println("  [2]  Deposit to Customer Account        ");
        System.out.println("  [3]  Withdraw from Customer Account     ");
        System.out.println("  [4]  Logout                             ");
        System.out.println("================================================");
        System.out.print("  Enter choice: ");
    }
    
}
