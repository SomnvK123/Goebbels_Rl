public class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;

    public BankAccount() {};

    public BankAccount(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    //deposit
    public void deposit (double amount){
        if (amount >= 0) {
            balance += amount;
            System.out.println("Deposited " + amount + " to " + ownerName);
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    public void withdraw (double amount){
        if (amount >= 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn " + amount + " to " + ownerName);
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    //get blance
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String toString() {
        return "AccountNumber: " + accountNumber + ", Owner: " + ownerName + ", Balance: " + balance;
    }
}