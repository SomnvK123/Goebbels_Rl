public class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    public CheckingAccount() {}

    public CheckingAccount(String accountNumber, String ownerName, double balance , double overdraftLimit) {
        super(accountNumber, ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance() + overdraftLimit) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawn " + amount + " successfully.");
        } else {
            System.out.println("Amount exceeds overdraft limit or invalid.");
        }
    }
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Checking, Overdraft Limit: " + overdraftLimit;
    }
}