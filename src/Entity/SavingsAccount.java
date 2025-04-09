package Entity;

public class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount() {}

    public SavingsAccount(String accountNumber, String ownerName, double balance, double interestRate) {
        super(accountNumber, ownerName, balance);
        this.interestRate = interestRate;
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