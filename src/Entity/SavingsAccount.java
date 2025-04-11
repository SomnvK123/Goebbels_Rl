package Entity;

import CustomeException.InvalidAmountException;

public class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount() {}

    public SavingsAccount(String accountNumber, String ownerName, double balance, double interestRate) {
        super(accountNumber, ownerName, balance);
        this.interestRate = interestRate;
    }

    // calculate interest
    public double calculateInterest(int months) {
        return getBalance() * (Math.pow(1 + interestRate, months) - 1) ;
    }
    // A=P×(1+r) ^n

    // apply interest to account
    public void applyInterest(int months) {
        double interest = calculateInterest(months);
        try {
            deposit(interest);
        } catch (InvalidAmountException e) {
            System.out.println("❌ Không thể cộng lãi suất: " + e.getMessage());
        }
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