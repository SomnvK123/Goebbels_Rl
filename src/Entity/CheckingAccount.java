package Entity;

public class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String ownerName, double balance , double overdraftLimit) {
        super(accountNumber, ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public synchronized void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance() + overdraftLimit) {
            System.out.println(Thread.currentThread().getName() +
                    " đang rút: " + amount + " | Số dư hiện tại: " + getBalance());

            try {
                Thread.sleep(5000); // Giả lập độ trễ
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            setBalance(getBalance() - amount);

            System.out.println(Thread.currentThread().getName() +
                    " đã rút: " + amount + " | Số dư sau rút: " + getBalance());
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " KHÔNG thể rút. Số dư: " + getBalance() +
                    ", Giới hạn thấu chi: " + overdraftLimit);
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