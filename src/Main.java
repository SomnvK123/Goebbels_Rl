import Entity.*;
import Enum.Action;
import Exception.*;
import Manager.BankManager;
import Validate.*;
import static Validate.Validate.*;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final BankManager banks = new BankManager();

    public static void main(String[] args) {
        // testRaceCondition();
        while (true) {
            showMenu();
            int choice = sc.nextInt();
            sc.nextLine();
            try {
                Action action = Action.getAction(choice);
                switch (action) {
                    case ADD_BANK_ACCOUNT -> {
                        addBankAccount();
                    }
                    case UPDATE_BANK_ACCOUNT -> {
                        updateBankAccount();
                    }
                    case LIST_BANK_ACCOUNTS -> {
                        banks.printBankAccounts();
                    }
                    case DELETE_BANK_ACCOUNT -> {

                    }
                    case DEPOSIT_MONEY -> {
                        depositMoney();
                    }
                    case WITHDRAW_MONEY -> {
                        withdrawMoney();
                    }
                    case TRANSFER_MONEY -> {
                        transferMoneyMain();
                    }
                    case EXIT -> {
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid choice. Please try again." + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nBank Management System");
        for (Action action : Action.values()) {
            System.out.println(action.getValue() + ": " + action.getDescription());
        }
        System.out.print("Enter your choice: ");
    }

    // input account type: saving account || checking account
    private static int inputAccountType() {
        // Choose account type
        System.out.println("Choose account type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");

        int type;
        while (true) {
            try {
                type = Integer.parseInt(sc.nextLine());
                if (type != 1 && type != 2) {
                    System.out.println("Please enter 1 or 2.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter 1 or 2.");
            }
        }
        return type;
    }

    // create account by type
    private static BankAccount createAccountByType(int type, String ownerName, double balance, String accountNumber) {
        if (type == 1) {
            System.out.print("Enter interest rate (/month): ");
            double rate = Double.parseDouble(sc.nextLine());
            return new SavingsAccount(accountNumber, ownerName, balance, rate);
        } else {
            System.out.print("Enter overdraft limit: ");
            double limit = Double.parseDouble(sc.nextLine());
            return new CheckingAccount(accountNumber, ownerName, balance, limit);
        }
    }

    private static void addBankAccount() {
        try {
            String accountNumber;
            // checking account
            while (true) {
                accountNumber = getInput("Enter account number: ");
                try {
                    checkingAccount1(accountNumber);
                    break;
                } catch (Exception e) {
                    System.out.println("Account number " + accountNumber + " is already exist. Please enter again.");
                }
            }

            // input data
            String ownerName = getInput("Enter owner name: ");
            double balance = inputBalance();
            int type = inputAccountType();

            // Tạo và thêm tài khoản mới
            BankAccount account = createAccountByType(type, ownerName, balance, accountNumber);
            banks.addBankAccount(account);

            System.out.println("Bank Account added successfully!");

        } catch (Exception e) {
            System.out.println("Error while adding bank account: " + e.getMessage());
        }
    }

    private static void updateBankAccount() {
        try {
            // input
            String oldAccountNumber;
            while (true) {
                oldAccountNumber = getInput("Enter current account number to update: ");
                try {
                    checkingAccount(oldAccountNumber); // Checking account exist
                    break;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // input data
            String newAccountNumber = getInput("Enter new account number: ");
            String newOwnerName = getInput("Enter new owner name: ");

            // update bank account
            boolean updated = banks.updateBankAccount(oldAccountNumber, newAccountNumber, newOwnerName);
            if (updated) {
                System.out.println("Bank account updated successfully!");
                banks.displayAccountDetails(newAccountNumber);
            } else {
                System.out.println("Failed to update account.");
            }
        } catch (Exception e) {
            System.out.println("Error while updating bank account: " + e.getMessage());
        }
    }

    private static void deleteBankAccount() {
        String accountNumber = getInput("Enter account number to delete: ");
        if (banks.removeBankAccount(accountNumber)) {
            System.out.println("Bank account deleted successfully!");
        } else {
            System.out.println("Bank account not found.");
        }
    }

    private static void depositMoney() {
        try {
            String accountNumber = getInput("Enter account number: ");
            // check account number exist
            checkingAccount(accountNumber);
            banks.displayAccountDetails(accountNumber);
            // find bank account by account number
            BankAccount account = banks.searchBankAccount(accountNumber);
            if (account == null) {
                System.out.println("Account number " + accountNumber + " not found.");
                return;
            }
            System.out.print("Enter amount to deposit: ");
            double amount = Double.parseDouble(sc.nextLine());
            account.deposit(amount);
            banks.displayAccountDetails(accountNumber); // list account detail
        } catch (Exception e) {
            System.out.println("Error while depositing bank account: " + e.getMessage());
        }
    }

    private static void withdrawMoney() {
        try {
            String accountNumber = getInput("Enter account number: ");
            // check account number exist
            checkingAccount(accountNumber);
            banks.displayAccountDetails(accountNumber);
            // find bank account by account number
            BankAccount account = banks.searchBankAccount(accountNumber);
            if (account == null) {
                System.out.println("Account number " + accountNumber + " not found.");
                return;
            }
            System.out.print("Enter amount to withdraw: ");
            double amount = Double.parseDouble(sc.nextLine());
            account.withdraw(amount);
            banks.displayAccountDetails(accountNumber);
        } catch (Exception e) {
            System.out.println("Error while withdrawing bank account: " + e.getMessage());
        }
    }

    private static void transferMoneyMain()
            throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
        String fromAccountNumber = getInputFromAccount("Enter account number to transfer from: ");
        BankAccount fromAccount = banks.findBankAccount(fromAccountNumber);
        System.out.println("From account [" + fromAccount.getAccountNumber() + " - " + fromAccount.getOwnerName() + "] to transfer successfully!");
        String toAccountNumber = getInputToAccount("Enter account number to transfer to: ");
        BankAccount toAccount = banks.findBankAccount(toAccountNumber);
        // cannot transfer to yourself
        if (fromAccountNumber.equals(toAccountNumber)) {
            String msg = "Cannot transfer to the same account.";
            System.out.println(msg);
            FileLogger.errorLog(msg);
            return;
        }

        double amount = getValidTransferAmount("Enter amount to transfer: ", fromAccount);
        logToFile(fromAccount, toAccount, amount);
    }

    // log to file
    private static void logToFile(BankAccount fromAccount, BankAccount toAccount, double amount) {
        try {
            banks.transferMoney(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount);
            FileLogger.transactionLog(String.format(
                    "Transfer $%.2f from [%s - %s] to [%s - %s]",
                    amount,
                    fromAccount.getAccountNumber(),
                    fromAccount.getOwnerName(),
                    toAccount.getAccountNumber(),
                    toAccount.getOwnerName()));
            System.out.println("Transfer successful!");
        } catch (InsufficientFundsException | InvalidAmountException | AccountNotFoundException e) {
            System.out.println("Error while transferring money: " + e.getMessage());
            FileLogger.errorLog(String.format(
                    "Transfer failed from [%s - %s] to [%s - %s]: %s",
                    fromAccount.getAccountNumber(),
                    fromAccount.getOwnerName(),
                    toAccount.getAccountNumber(),
                    toAccount.getOwnerName(),
                    e.getMessage()));
        }
    }

    // test race condition
    private static void testRaceCondition() throws InvalidAmountException, InsufficientFundsException {
        BankAccount account = new CheckingAccount("123", "Test User", 1000, 0); // số dư ban đầu 1000

        Runnable withdrawTask = () -> {
            for (int i = 0; i < 2; i++) {
                try {
                    account.withdraw(300); // mỗi thread cố rút 300
                } catch (InsufficientFundsException e) {
                    throw new RuntimeException(e);
                } catch (InvalidAmountException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread t1 = new Thread(withdrawTask, "T1");
        Thread t2 = new Thread(withdrawTask, "T2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Số dư cuối cùng: " + account.getBalance());
    }

}