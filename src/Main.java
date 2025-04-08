import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addBankAccount() {
        try {
            // Input data
            System.out.print("Enter account number: ");
            String accountNumber = sc.nextLine();

            // Check if account exists
            BankAccount existingAccount = banks.searchBankAccount(accountNumber);
            if (existingAccount != null) {
                System.out.println("Account with ID " + accountNumber + " already exists.");
                return;
            }

            System.out.print("Enter full name: ");
            String ownerName = sc.nextLine();

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

            BankAccount account = null;

            if (type == 1) {
                System.out.print("Enter interest rate (e.g., 0.05 for 5%): ");
                double rate = Double.parseDouble(sc.nextLine());
                account = new SavingsAccount(accountNumber, ownerName, balance, rate);
            } else {
                System.out.print("Enter overdraft limit: ");
                double limit = Double.parseDouble(sc.nextLine());
                account = new CheckingAccount(accountNumber, ownerName, balance, limit);
            }

            banks.addBankAccount(account);
            System.out.println("Bank Account added successfully!");
        } catch (Exception e) {
            System.out.println("Error while adding bank account: " + e.getMessage());
        }
    }

    private static void updateBankAccount() {
        System.out.print("Enter current account number to update: ");
        String oldAccountNumber = sc.nextLine();

        BankAccount existingAccount = banks.searchBankAccount(oldAccountNumber);
        if (existingAccount == null) {
            System.out.println("Account with ID " + oldAccountNumber + " not found.");
            return;
        }

        System.out.print("Enter new account number: ");
        String newAccountNumber = sc.nextLine();

        System.out.print("Enter new full name: ");
        String newOwnerName = sc.nextLine();

        try {
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


    private static void deleteBankAccount() {
        System.out.print("Enter account number to delete: ");
        String accountNumber = sc.nextLine();
        if (banks.deleteBankAccount(accountNumber)) {
            System.out.println("Bank account deleted successfully!");
        } else {
            System.out.println("Bank account not found.");
        }
    }

    private static void depositMoney() {
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        //check account number exist
        BankAccount existingAccount = banks.searchBankAccount(accountNumber);
        if (existingAccount == null) {
            System.out.println("Account number " + accountNumber + " not found.");
            return;
        } else {
            System.out.println("Account number " + accountNumber + " founded.");
            banks.displayAccountDetails(accountNumber);
        }

        BankAccount account = banks.findBankAccount(accountNumber);
        if (account == null) {
            System.out.println("Account number " + accountNumber + " not found.");
            return;
        }

        System.out.print("Enter amount to deposit: ");
        double amount = Double.parseDouble(sc.nextLine());
        account.deposit(amount);
        banks.displayAccountDetails(accountNumber);
    }

    private static void withdrawMoney() {
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        //check account number exist
        BankAccount existingAccount = banks.searchBankAccount(accountNumber);
        if (existingAccount == null) {
            System.out.println("Account number " + accountNumber + " not found.");
            return;
        } else {
            System.out.println("Account number " + accountNumber + " founded.");
            banks.displayAccountDetails(accountNumber);
        }

        BankAccount account = banks.findBankAccount(accountNumber);
        if (account == null) {
            System.out.println("Account number " + accountNumber + " not found.");
            return;
        }

        System.out.print("Enter amount to withdraw: ");
        double amount = Double.parseDouble(sc.nextLine());
        account.withdraw(amount);
        banks.displayAccountDetails(accountNumber);
    }

}