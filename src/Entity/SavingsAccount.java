package Entity;

public class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount() {}

    public SavingsAccount(String accountNumber, String ownerName, double balance, double interestRate) {
        super(accountNumber, ownerName, balance);
        this.interestRate = interestRate;
    }

    public double calculateInterest(int months) {
        return getBalance() *Math.pow(1 + interestRate, months) ;
    }
    // A=P×(1+r) ^n

    // apply interest to account
    public void applyInterest(int months) {
        double interest = calculateInterest(months);
        deposit(interest);
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Savings, Interest Rate: " + interestRate;
    }
}