package Manager;

import Entity.BankAccount;
import Entity.CheckingAccount;
import Entity.SavingsAccount;

import java.util.ArrayList;
import java.util.List;

public class BankManager {
    private static final List<BankAccount> banks = new ArrayList<BankAccount>();

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
            System.out.println("⚠️  No bank accounts found.");
            return;
        }

        String headerTop = "╔══════════════════════╦═════════════════════════╦══════════════╦═════════════════════╦════════════════════════════════════╗";
        String headerMid = "║ Account Number       ║ Owner Name              ║ Balance      ║ Account Type        ║ Extra Info                         ║";
        String headerSep = "╠══════════════════════╬═════════════════════════╬══════════════╬═════════════════════╬════════════════════════════════════╣";
        String footerLine = "╚══════════════════════╩═════════════════════════╩══════════════╩═════════════════════╩════════════════════════════════════╝";

        printWithDelay(headerTop, 2);
        printWithDelay(headerMid, 2);
        printWithDelay(headerSep, 2);

        for (BankAccount bank : banks) {
            String type = "Basic Account";
            String extra = "N/A";

            if (bank instanceof SavingsAccount) {
                type = "Savings Account";
                extra = "Interest Rate: " + ((SavingsAccount) bank).getInterestRate() + " / month";
            } else if (bank instanceof CheckingAccount) {
                type = "Checking Account";
                extra = "Overdraft Limit: $" + ((CheckingAccount) bank).getOverdraftLimit();
            }

            String row = String.format("║ %-20s ║ %-23s ║ $%-11.2f ║ %-19s ║ %-32s ║",
                    bank.getAccountNumber(),
                    bank.getOwnerName(),
                    bank.getBalance(),
                    type,
                    extra
            );
            printWithDelay(row, 2);
        }

        printWithDelay(footerLine, 2);
    }

    // In từng dòng với hiệu ứng gõ phím
    private void printWithDelay(String text, int delayMillis) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMillis); // delay nhỏ giữa mỗi ký tự
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    public void displayAccountDetails(String accountNumber) {
        BankAccount account = searchBankAccount(accountNumber);
        if (account == null) {
            System.out.println("⚠️  Account number " + accountNumber + " not found.");
            return;
        }

        // Chuẩn bị dữ liệu
        String type;
        String extra;

        if (account instanceof SavingsAccount) {
            type = "Savings Account";
            extra = "Interest Rate: " + ((SavingsAccount) account).getInterestRate() + " / month";
        } else if (account instanceof CheckingAccount) {
            type = "Checking Account";
            extra = "Overdraft Limit: $" + ((CheckingAccount) account).getOverdraftLimit();
        } else {
            type = "Basic Account";
            extra = "N/A";
        }

        // In giao diện mô phỏng UI
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             🌟 ACCOUNT DETAILS 🌟                                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf ("║ %-20s : %-50s ║\n", "Account Number", account.getAccountNumber());
        System.out.printf ("║ %-20s : %-50s ║\n", "Owner Name", account.getOwnerName());
        System.out.printf ("║ %-20s : $%-49.2f ║\n", "Balance", account.getBalance());
        System.out.printf ("║ %-20s : %-50s ║\n", "Account Type", type);
        System.out.printf ("║ %-20s : %-50s ║\n", "Extra Info", extra);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════╝");
    }

    public BankAccount searchBankAccount(String accountNumber) {
            for (BankAccount bank : banks) {
                if (bank.getAccountNumber().equals(accountNumber)) {
                    return bank;
                }
            }
            return null;
        }
}