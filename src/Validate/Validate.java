package Validate;

import Entity.*;
import Manager.BankManager;
import java.util.Scanner;
import Exception.*;

public class Validate {

    private static final Scanner sc = new Scanner(System.in);
    private static final BankManager banks = new BankManager();

    // input
    public static String getInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
    public static double inputBalance() throws InvalidAmountException{
        double balance;
        while (true) {
            System.out.print("Enter balance: ");
            try {
                balance = Double.parseDouble(sc.nextLine());
                if (balance < 0) {
                    throw new InvalidAmountException("Amount must be greater than 0.");
                } else {
                    return balance;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            } catch (InvalidAmountException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    // check account exist
    public static void checkingAccount(String accountNumber) throws AccountNotFoundException {
        BankAccount existingAccount = banks.searchBankAccount(accountNumber);
        if (existingAccount == null ) {
            throw new AccountNotFoundException("Account number " + accountNumber + " not found.");
        }
    }

    public static void checkingAccount1(String accountNumber) throws Exception {
        BankAccount existingAccount = banks.searchBankAccount(accountNumber);
        if (existingAccount != null ) {
            throw new Exception("Account number " + accountNumber + " is already exist.");
        }
    }

    // check account valid
    public static String getInputFromAccount(String prompt) throws AccountNotFoundException {
        String fromAccountNumber;
        BankAccount fromAccount = null;

        while (true) {
            fromAccountNumber = getInput(prompt);
            try {
                checkingAccount(fromAccountNumber);
                fromAccount = banks.searchBankAccount(fromAccountNumber);
                System.out.println("From: " + fromAccountNumber + " | Owner: " + fromAccount.getOwnerName());
                break;
            } catch (AccountNotFoundException e) {
                System.out.println("Account number " + fromAccount.getOwnerName() + " does not exist. Please enter again.");
                FileLogger.errorLog(e.getMessage());
            }
        }
        return fromAccount.getAccountNumber();
    }
    public static String getInputToAccount(String prompt) throws AccountNotFoundException {
        String toAccountNumber;
        BankAccount toAccount = null;
        while (true) {
            toAccountNumber = getInput(prompt);
            try {
                checkingAccount(toAccountNumber);
                toAccount = banks.searchBankAccount(toAccountNumber);
                if (toAccount != null) {
                    System.out.println("To: " + toAccountNumber + " | Owner: " + toAccount.getOwnerName());
                    break;
                } else {
                    throw new AccountNotFoundException("Account number " + toAccountNumber + " does not exist.");
                }
            } catch (AccountNotFoundException e) {
                System.out.println("Account number " + toAccountNumber + " does not exist. Please enter again.");
                FileLogger.errorLog(e.getMessage());
            }
        }
        return toAccount.getAccountNumber();
    }


    public static double getValidTransferAmount(String prompt, BankAccount fromAccount) throws InvalidAmountException, InsufficientFundsException {
        double amount = 0;
       while (true) {
            System.out.print(prompt);
            try {
                amount = Double.parseDouble(sc.nextLine());
                if (amount < 0) {
                    throw new InvalidAmountException("Amount must be greater than 0..");
                } else {
                    if (amount > fromAccount.getBalance()) {
                        throw new InsufficientFundsException("Withdrawing money with an amount greater than the balance.");
                    }
                    else {
                        return amount;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            } catch (InvalidAmountException | InsufficientFundsException e) {
                System.out.println(e.getMessage());
                FileLogger.errorLog("Errpr: " + e.getMessage());
            }
        }
    }
}
