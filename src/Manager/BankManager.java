package Manager;

import Entity.*;
import Exception.*;
import Validate.FileLogger;

import java.util.*;

public class BankManager {
    private static final Map<String, BankAccount> bankAccounts = new HashMap<>();

    public synchronized void addBankAccount(BankAccount bankAccount) {
        bankAccounts.put(bankAccount.getAccountNumber(), bankAccount);
    }

    public synchronized boolean updateBankAccount(String oldAccountNumber, String newAccountNumber, String newOwnerName) {
        // Check trùng số tài khoản mới
        if (bankAccounts.containsKey(newAccountNumber) && !oldAccountNumber.equals(newAccountNumber)) {
            System.out.println("New account number already exists.");
            return false;
        }

        BankAccount account = bankAccounts.remove(oldAccountNumber);
        if (account != null) {
            account.setAccountNumber(newAccountNumber);
            account.setOwnerName(newOwnerName);
            bankAccounts.put(newAccountNumber, account);
            return true;
        }

        return false;
    }

    public synchronized boolean removeBankAccount(String accountNumber) {
        if (bankAccounts.containsKey(accountNumber)) {
            bankAccounts.remove(accountNumber);
            return true;
        }
        return false;
    }

    public void printBankAccounts() {
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found!");
            return;
        }

        // In tiêu đề bảng
        System.out.printf("%-18s %-15s %-20s %-12s %-25s%n",
                "Account Type", "Account No.", "Owner", "Balance", "Details");
        System.out.println("-----------------------------------------------------------------------------------------");

        // In từng tài khoản
        for (BankAccount bank : bankAccounts.values()) {
            if (bank instanceof SavingsAccount sa) {
                System.out.printf("%-18s %-15s %-20s %-12.2f %-25s%n",
                        "Savings Account", bank.getAccountNumber(), bank.getOwnerName(),
                        bank.getBalance(), "Interest Rate: " + String.format("%.2f", sa.getInterestRate()));
            } else if (bank instanceof CheckingAccount ca) {
                System.out.printf("%-18s %-15s %-20s %-12.2f %-25s%n",
                        "Checking Account", bank.getAccountNumber(), bank.getOwnerName(),
                        bank.getBalance(), "Overdraft Limit: " + String.format("%.2f", ca.getOverdraftLimit()));
            }
        }
    }

    public BankAccount searchBankAccount(String accountNumber) {
        return bankAccounts.get(accountNumber);
    }

    public static BankAccount findBankAccount(String accountNumber) throws AccountNotFoundException {
        BankAccount account = bankAccounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    public void displayAccountDetails(String accountNumber) throws AccountNotFoundException {
        BankAccount account = searchBankAccount(accountNumber);
        if (account == null) {
            System.out.println("Account number: " + accountNumber + " not exist.");
            return;
        }

        System.out.printf("%-18s %-15s %-20s %-12s %-25s%n",
                "Type", "Account No.", "Owner", "Balance", "Extra Info");
        System.out.println("-------------------------------------------------------------------");

        if (account instanceof SavingsAccount sa) {
            System.out.printf("%-18s %-15s %-20s %-12.2f %-25s%n",
                    "Savings Account", account.getAccountNumber(), account.getOwnerName(),
                    account.getBalance(), "Interest Rate: " + String.format("%.2f", sa.getInterestRate()));
        } else if (account instanceof CheckingAccount ca) {
            System.out.printf("%-18s %-15s %-20s %-12.2f %-25s%n",
                    "Checking Account", account.getAccountNumber(), account.getOwnerName(),
                    account.getBalance(), "Overdraft Limit: " + String.format("%.2f", ca.getOverdraftLimit()));
        }
    }

    public static void transferMoney(String fromAccountNumber, String toAccountNumber, double amount)
            throws InsufficientFundsException, AccountNotFoundException, InvalidAmountException {

        BankAccount from = findBankAccount(fromAccountNumber);
        BankAccount to = findBankAccount(toAccountNumber);

        if (amount <= 0) {
            String message = "Amount must be greater than 0.";
            FileLogger.errorLog("Transfer failed from [" + fromAccountNumber + "] to [" + toAccountNumber + "]: " + message);
            throw new InvalidAmountException(message);
        }

        if (from.getBalance() < amount) {
            String message = "Insufficient funds in account: " + fromAccountNumber;
            FileLogger.errorLog("Transfer failed from [" + fromAccountNumber + " - " + from.getOwnerName() + "] to [" +
                    toAccountNumber + " - " + to.getOwnerName() + "]: " + message);
            throw new InsufficientFundsException(message);
        }

        synchronized (from) {
            synchronized (to) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
