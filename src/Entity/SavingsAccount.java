package Entity;

public class SavingsAccount extends BankAccount {
    private double interestRate;

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

    @Override
    public synchronized void withdraw(double amount){
        if (amount >= 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawn " + amount + " to " + getOwnerName());
        } else {
            System.out.println("Can't withdraw " + amount + " from " + getOwnerName() + " because your balance have: " + getBalance());
        }
    }
}