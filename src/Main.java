import CustomeException.AccountNotFoundException;
import CustomeException.InsufficientFundsException;
import CustomeException.InvalidAmountException;
import CustomeException.SameAccountTransferException;
import Entity.BankAccount;
import Entity.CheckingAccount;
import Entity.SavingsAccount;
import Manager.BankManager;
import Utils.Filelog;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final BankManager banks = new BankManager();

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice;
            try {
                choice = sc.nextInt();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 0 and 8.");
                continue;
            }

            sc.nextLine();

            switch (choice) {
                case 1:
                    addBankAccount();
                    break;
                case 2:
                    updateBankAccount();
                    break;
                case 3:
                    deleteBankAccount();
                    break;
                case 4:
                    banks.listBankAccounts();
                    break;
                case 5:
                    depositMoney();
                    break;
                case 6:
                    withdrawMoney();
                    break;
                case 7:
                    calculateInterest();
                    break;
                case 8:
                    transferMoneyMain();
                    break;
                case 0:
                    System.out.println("End program");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nBank Management System");
        System.out.println("1. Add Bank Account");
        System.out.println("2. Update Bank Account");
        System.out.println("3. Delete Bank Account");
        System.out.println("4. List All Bank Accounts");
        System.out.println("5. Deposit Money");
        System.out.println("6. Withdraw Money");
        System.out.println("7. Calculate Interest");
        System.out.println("8. Transfer Money");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // input
    private static String getInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static double inputBalance() {
        double balance;
        while (true) {
            System.out.print("Enter balance: ");
            try {
                balance = Double.parseDouble(sc.nextLine());
                if (balance < 0) {
                    System.out.println("Balance must be >= 0. Please try again.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
        return balance;
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

    // check account exist
    private static void checkingAccount(String accountNumber) throws AccountNotFoundException {
        BankAccount existingAccount = banks.searchBankAccount(accountNumber);
        if (existingAccount == null) {
            throw new AccountNotFoundException("Account number " + accountNumber + " not found.");
        }
    }

    // check account exist: use for add bank account
    private static void checkingAccount1(String accountNumber) throws Exception {
        BankAccount existingAccount = banks.searchBankAccount(accountNumber);
        if (existingAccount != null) {
            throw new Exception("Account number " + accountNumber + " is already exist.");
        }
    }

    // check account number for update
    private static void checkingAccountForUpdate(String oldAccountNumber, String newAccountNumber) throws Exception {
        checkingAccount(oldAccountNumber); // Kiểm tra oldAccountNumber có tồn tại không
        // Nếu newAccountNumber khác old và đã tồn tại, báo lỗi
        if (!newAccountNumber.equals(oldAccountNumber) &&
                banks.searchBankAccount(newAccountNumber) != null) {
            throw new Exception("New account number already exists.");
        }
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
            // Input data
            String accountNumber;
            while (true) {
                accountNumber = getInput("Enter account number: ");
                try {
                    checkingAccount1(accountNumber);
                    break;
                } catch (Exception e) {
                    System.out.println("Account number " + accountNumber + " is already exist. Please enter again.");
                }
            }
            // input
            String ownerName = getInput("Enter owner name: ");
            double balance = inputBalance();
            // Choose account type
            int type = inputAccountType();
            // add bank account
            BankAccount account = createAccountByType(type, ownerName, balance, accountNumber);
            banks.addBankAccount(account);
            System.out.println("Bank Account added successfully!");
        } catch (Exception e) {
            System.out.println("Error while adding bank account: " + e.getMessage());
        }
    }

    // update bank account
    private static void updateBankAccount() {
        try {
            // input
            String oldAccountNumber;
            while (true) {
                oldAccountNumber = getInput("Enter current account number to update: ");
                try {
                    // check account number exist
                    checkingAccount(oldAccountNumber);
                    break;
                } catch (Exception e) {
                    System.out.println("Account number " + oldAccountNumber + " is not exist. Please enter again.");
                }
            }
            // input
            String newAccountNumber;
            while (true) {
                newAccountNumber = getInput("Enter new account number: ");
                try {
                    checkingAccountForUpdate(oldAccountNumber, newAccountNumber);
                    break;
                } catch (Exception e) {
                    System.out.println("Account number " + newAccountNumber + " is already exist. Please enter again.");
                }
            }
            String newOwnerName = getInput("Enter new owner name: ");
            // update bank account
            if (banks.updateBankAccount(oldAccountNumber, newAccountNumber, newOwnerName)) {
                System.out.println("Bank account updated successfully!");
                banks.displayAccountDetails(newAccountNumber);
            } else {
                System.out.println("Failed to update account.");
            }
        } catch (Exception e) {
            System.out.println("Error while updating bank account: " + e.getMessage());
        }
    }

    // delete bank account
    private static void deleteBankAccount() {
        String accountNumber = getInput("Enter account number to delete: ");
        if (banks.deleteBankAccount(accountNumber)) {
            System.out.println("Bank account deleted successfully!");
        } else {
            System.out.println("Bank account not found.");
        }
    }

    // deposit money
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

    // withdraw money
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

    // calculate interest
    private static void calculateInterest() {
        try {
            String accountNumber = getInput("Enter account number: ");
            checkingAccount(accountNumber);
            BankAccount account = banks.searchBankAccount(accountNumber);
            if (account == null) {
                System.out.println("Account number " + accountNumber + " not found.");
                return;
            }
            System.out.print("Enter months to calculate interest: ");
            int months = Integer.parseInt(sc.nextLine());
            SavingsAccount saving = (SavingsAccount) account;
            double calculate = saving.calculateInterest(months);
            System.out.print("Enter interest: " + calculate);
        } catch (Exception e) {
            System.out.println("Error while calculate: " + e.getMessage());
        }
    }

    // transfer money
    private static void transferMoneyMain() {
        String fromAccountNumber = getValidAccount("Enter account number to transfer from: ", true);
        BankAccount fromAccount = banks.searchBankAccount(fromAccountNumber);

        String toAccountNumber = getValidAccount("Enter account number to transfer to: ", false);
        BankAccount toAccount = banks.searchBankAccount(toAccountNumber);

        double amount = getValidTransferAmount(fromAccount);

        executeTransfer(fromAccount, toAccount, amount);
    }

    // check money transfer valid
    private static double getValidTransferAmount(BankAccount fromAccount) {
        double amount = 0;
        while (true) {
            System.out.print("Enter amount to transfer: ");
            try {
                amount = Double.parseDouble(sc.nextLine());
                if (amount <= 0) {
                    throw new InvalidAmountException(
                            "Error: Sender: " + fromAccount.getOwnerName() + " Amount must be greater than 0.");
                } else if (fromAccount.getBalance() < amount) {
                    throw new InsufficientFundsException(
                            "The amount you are trying to transfer exceeds your account balance: Sender: "
                                    + fromAccount.getAccountNumber() + " Fullname: " + fromAccount.getOwnerName()
                                    + " | Amount: " + amount);
                } else {
                    return amount;
                }
            } catch (InvalidAmountException | InsufficientFundsException | NumberFormatException e) {
                System.out.println("Error " + e.getMessage());
                Filelog.logError("Invalid amount: " + e.getMessage());
            }
        }
    }

    // log transfer && log error
    private static void executeTransfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        try {
            banks.transferMoney(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount);
            Filelog.logTransaction(String.format(
                    "Transfer $%.2f from [%s - %s] to [%s - %s]",
                    amount,
                    fromAccount.getAccountNumber(),
                    fromAccount.getOwnerName(),
                    toAccount.getAccountNumber(),
                    toAccount.getOwnerName()));
            System.out.println("✅ Transfer successful!");
        } catch (InsufficientFundsException | InvalidAmountException | AccountNotFoundException
                | SameAccountTransferException e) {
            System.out.println("Error while transferring money: " + e.getMessage());
            Filelog.logError(String.format(
                    "Transfer failed from [%s - %s] to [%s - %s]: %s",
                    fromAccount.getAccountNumber(),
                    fromAccount.getOwnerName(),
                    toAccount.getAccountNumber(),
                    toAccount.getOwnerName(),
                    e.getMessage()));
        }
    }

    // check account valid
    private static String getValidAccount(String prompt, boolean isSender) {
        String accountNumber;
        BankAccount account = null;

        while (true) {
            accountNumber = getInput(prompt);
            try {
                checkingAccount(accountNumber);
                account = banks.searchBankAccount(accountNumber);
                System.out.println(
                        (isSender ? "From" : "To") + ": " + accountNumber + " | Owner: " + account.getOwnerName());
                return accountNumber;
            } catch (AccountNotFoundException e) {
                String role = isSender ? "sender" : "receiver";
                String error = "Account number of " + role + " " + accountNumber
                        + " does not exist. Please enter again.";
                System.out.println(error);
                Filelog.logError(error + " Exception: " + e.getMessage());
            }
        }
    }
};