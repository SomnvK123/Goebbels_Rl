package Entity;

import Exception.*;
import Validate.*;

public abstract class BankAccount implements DoBank {
    private String accountNumber;
    private String ownerName;
    private double balance;

    public BankAccount(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    @Override
    // deposit
    public void deposit(double amount) throws InvalidAmountException {
        if (amount < 0) {
            String message = "Deposit money must be > 0";
            System.out.println(message);
            FileLogger.errorLog(message);
            throw new InvalidAmountException(message);
        }
        if (amount >= 0) {
            balance += amount;
            System.out.println("Deposited " + amount + " to " + ownerName);
        } else {
            System.out.println("Cannot deposit " + amount + " to " + ownerName + " because the amount is negative");
        }
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            String message = "Withdraw money must be > 0";
            System.out.println(message);
            FileLogger.errorLog(message);
            throw new InvalidAmountException(message);
        }
        if (amount > balance) {
            String message = "Cannot withdraw " + amount + " from account of " + ownerName + ". Balance now: " + balance;
            System.out.println(message);
            FileLogger.errorLog(message);
            throw new InsufficientFundsException(message);
        }

        balance -= amount;
        String successMessage = "Đã rút " + amount + " từ tài khoản [" + accountNumber + " - " + ownerName + "]";
        System.out.println(successMessage);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                '}';
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}