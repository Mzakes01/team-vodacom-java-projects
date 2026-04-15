package banking.model;

public class SavingsAccount extends BankAccount {
    //The minimum balance that must remain in the account at all times.
     

    private final double minimumBalance;

    // Constructor
    public SavingsAccount(String accountNumber, String ownerName, double initialBalance, double minimumBalance) {
        super(accountNumber, ownerName, initialBalance);
        this.minimumBalance = minimumBalance;
    }

    // Getter
    public double getMinimumBalance() {
        return minimumBalance;
    }

    // Override withdraw method
    @Override
   
    public void withdraw(double amount) throws InsufficientFundsException {
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (getBalance() - amount < minimumBalance) {
            throw new InsufficientFundsException(amount, getBalance() - minimumBalance);
        }

        // Deduct money
        deductFromBalance(amount);

        // Record transaction
        Transaction transaction = new Transaction(
                Transaction.Type.WITHDRAWAL,
                amount,
                getBalance()
        );

        addTransaction(transaction);
    }

    // toString method
    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountNumber='" + getAccountNumber() + '\'' +
                ", owner='" + getOwnerName() + '\'' +
                ", balance=" + getBalance() +
                ", minimumBalance=" + minimumBalance +
                '}';
    }
}
