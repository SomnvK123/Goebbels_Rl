package Entity;

import CustomeException.InsufficientFundsException;
import CustomeException.InvalidAmountException;

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
    public void deposit (double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền gửi phải lớn hơn 0");
        }
        if (amount >= 0) {
            balance += amount;
            System.out.println("Deposited " + amount + " to " + ownerName);
        } else {
            System.out.println("Cannot deposit " + amount + " to " + ownerName + " because the amount is negative");
        }
    }

// Withdraw for saving account
public void withdraw(double amount) throws InsufficientFundsException, InvalidAmountException {
    if (amount <= 0) {
        throw new InvalidAmountException("Số tiền rút phải lớn hơn 0");
    }

    if (amount > balance) {
        throw new InsufficientFundsException("Không thể rút " + amount + " từ tài khoản của " + ownerName + ". Số dư hiện tại: " + balance);
    }

    balance -= amount;
    System.out.println("Đã rút " + amount + " từ tài khoản của " + ownerName);
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