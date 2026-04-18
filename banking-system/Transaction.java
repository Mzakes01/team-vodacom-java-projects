/*
Telling Java to make sure that the class
 "Transaction" is stored within
  banking.model
*/
package banking.model;

/*
Java does not know the date & time automatically
so we will be using "LocalDateTime" so that it is able
to retrieve that information
*/
import java.time.LocalDateTime;
//Missing DateTimeFormatter import for proper timestamp formatting.

//Creating class named "Transaction"
public class Transaction {
    //Creating enum which is a fixed set of constant values 
    public enum Type {
        DEPOSIT,
        WITHDRAWAL
    }

/*
Creating private final variables which can only 
be accessed within this class and the value 
cannot be changed because we used "final"
*/
private final Type type;
private final double amount;
private final double balanceAfter;
private final LocalDateTime timestamp;

/*
Creating a constructor so we are able to store 
the data into instance variables
*/
public Transaction(Type type, double amount, double balanceAfter){
    this.type = type;
    this.amount = amount;
    this.balanceAfter = balanceAfter;
    this.timestamp = LocalDateTime.now();
}

//Creating a 'Getter' which basically returns stored values
public Type getType(){
    return type;
}

public double getAmount(){
    return amount; 
}

public double getBalanceAfter(){
    return balanceAfter;
}

public LocalDateTime getTimestamp(){
    return timestamp;
}

//Creating the format for the timestamp
@Override //Telling Java that this method is to replace the method from a parent class
public String toString(){
    return "[" + timestamp.toString().replace("T", " ").substring(0,16) + "] " // Use DateTimeFormatter instead of string manipulation
             + type + " R " + String.format("%.2f", amount)
             + " Balance: R " + String.format("%.2f", balanceAfter);
}
}
