package banking.model;

public class InsufficientFundsException extends Exception {
    private final double shortfall;
    
    public InsufficientFundsException(double amountRequested, double availableBalance) {
        super();
        this.shortfall = amountRequested - availableBalance;
    }
    
    @Override
    public String getMessage() {
        return String.format("Insufficient funds. You need R %.2f more to complete this withdrawal.", shortfall);
    }
    
    public double getShortfall() {
        return shortfall;
    }
}
