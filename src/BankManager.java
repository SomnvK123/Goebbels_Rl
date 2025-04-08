import java.util.ArrayList;
import java.util.List;

public class BankManager {
    private List<BankAccount> banks = new ArrayList<BankAccount>();

    public void addBankAccount(BankAccount bank) {
            banks.add(bank);
        };

    public boolean updateBankAccount(String oldAccountNumber, String newAccountNumber, String newOwnerName) {
        // Check account number exist
        for (BankAccount bank : banks) {
            if (bank.getAccountNumber().equals(newAccountNumber) && !bank.getAccountNumber().equals(oldAccountNumber)) {
                System.out.println("New account number already exists.");
                return false;
            }
        }

        // update information
        for (BankAccount bank : banks) {
            if (bank.getAccountNumber().equals(oldAccountNumber)) {
                bank.setAccountNumber(newAccountNumber);
                bank.setOwnerName(newOwnerName);
                return true;
            }
        }

        return false;
    }


    public boolean deleteBankAccount(String accountNumber) {
            for (BankAccount bank : banks) {
                if (bank.getAccountNumber().equals(accountNumber)) {
                    banks.remove(bank);
                    return true;
                }
            }
            return false;
        }

    public void listBankAccounts() {
        if (banks.isEmpty()) {
            System.out.println("No bank accounts.");
        } else {
            System.out.printf("%-20s %-25s %-15s %-20s %-20s\n",
                    "Account Number", "Owner Name", "Balance", "Account Type", "Extra Info");
            System.out.println("---------------------------------------------------------------------------------------------");

            for (BankAccount bank : banks) {
                String type = "Basic Account";
                String extra = "N/A";

                if (bank instanceof SavingsAccount) {
                    type = "Savings Account";
                    extra = "Interest Rate: " + ((SavingsAccount) bank).getInterestRate();
                } else if (bank instanceof CheckingAccount) {
                    type = "Checking Account";
                    extra = "Overdraft Limit: " + ((CheckingAccount) bank).getOverdraftLimit();
                }

                System.out.printf("%-20s %-25s %-15.2f %-20s %-20s\n",
                        bank.getAccountNumber(),
                        bank.getOwnerName(),
                        bank.getBalance(),
                        type,
                        extra);
            }
        }
    }


    public void displayAccountDetails(String accountNumber) {
        BankAccount account = searchBankAccount(accountNumber);
        if (account == null) {
            System.out.println("Account number " + accountNumber + " not found.");
            return;
        }
        System.out.println("Account found:");
        System.out.printf("%-20s %-25s %-15s %-20s %-20s\n",
                "Account Number", "Owner Name", "Balance", "Account Type", "Extra Info");
        System.out.println("---------------------------------------------------------------------------------------------");

        String type;
        String extra;

        if (account instanceof SavingsAccount) {
            type = "Savings Account";
            extra = "Interest Rate: " + ((SavingsAccount) account).getInterestRate();
        } else if (account instanceof CheckingAccount) {
            type = "Checking Account";
            extra = "Overdraft Limit: " + ((CheckingAccount) account).getOverdraftLimit();
        } else {
            type = "Basic Account";
            extra = "";
        }

        System.out.printf("%-20s %-25s %-15.2f %-20s %-20s\n",
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getBalance(),
                type,
                extra);
    }



    public BankAccount searchBankAccount(String accountNumber) {
            for (BankAccount bank : banks) {
                if (bank.getAccountNumber().equals(accountNumber)) {
                    return bank;
                }
            }
            return null;
        }

    public BankAccount findBankAccount(String accountNumber) {
        for (BankAccount bank : banks) {
            if (bank.getAccountNumber().equals(accountNumber)) {
                return bank;
            }
        }
        return null;
    }
}