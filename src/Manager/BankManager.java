package Manager;

import CustomeException.AccountNotFoundException;
import CustomeException.InsufficientFundsException;
import CustomeException.InvalidAmountException;
import CustomeException.SameAccountTransferException;
import Entity.BankAccount;
import Entity.CheckingAccount;
import Entity.SavingsAccount;
import Utils.Filelog;
import java.util.HashMap;
import java.util.Map;

public class BankManager {
    private static Map<String, BankAccount> banks = new HashMap<>();;

    public void addBankAccount(BankAccount bank) {
        banks.put(bank.getAccountNumber(), bank);
    };

    public boolean updateBankAccount(String oldAccountNumber, String newAccountNumber, String newOwnerName) {
        // Check account number exist
        if (!banks.containsKey(oldAccountNumber)) {
            System.out.println("Old account number not found.");
            return false;
        }
        if (banks.containsKey(newAccountNumber) && !newAccountNumber.equals(oldAccountNumber)) {
            System.out.println("New account number already exists.");
            return false;
        }

        // update information
        BankAccount bank = banks.remove(oldAccountNumber);
        bank.setAccountNumber(newAccountNumber);
        bank.setOwnerName(newOwnerName);
        banks.put(newAccountNumber, bank);
        return true;
    }

    public boolean deleteBankAccount(String accountNumber) {
        if (banks.containsKey(accountNumber)) {
            banks.remove(accountNumber);
            return true;
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

        for (BankAccount bank : banks.values()) {
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
                    extra);
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

        if (account instanceof SavingsAccount savingsAccount) {
            type = "Savings Account";
            extra = "Interest Rate: " + savingsAccount.getInterestRate() + " / month";
        } else if (account instanceof CheckingAccount checkingAccount) {
            type = "Checking Account";
            extra = "Overdraft Limit: $" + checkingAccount.getOverdraftLimit();
        } else {
            type = "Basic Account";
            extra = "N/A";
        }

        // In giao diện mô phỏng UI
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             🌟 ACCOUNT DETAILS 🌟                                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s : %-50s ║\n", "Account Number", account.getAccountNumber());
        System.out.printf("║ %-20s : %-50s ║\n", "Owner Name", account.getOwnerName());
        System.out.printf("║ %-20s : $%-49.2f ║\n", "Balance", account.getBalance());
        System.out.printf("║ %-20s : %-50s ║\n", "Account Type", type);
        System.out.printf("║ %-20s : %-50s ║\n", "Extra Info", extra);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════╝");
    }

    public BankAccount searchBankAccount(String accountNumber) {
        if (banks.containsKey(accountNumber)) {
            return banks.get(accountNumber);
        }
        return null;
    }

    public static BankAccount findBankAccount(String accountNumber) throws AccountNotFoundException {
        BankAccount account = banks.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    public static void transferMoney(String fromAccountNumber, String toAccountNumber, double amount)
            throws InsufficientFundsException, AccountNotFoundException, InvalidAmountException,
            SameAccountTransferException {

        BankAccount from = findBankAccount(fromAccountNumber);
        BankAccount to = findBankAccount(toAccountNumber);

        if (fromAccountNumber.equals(toAccountNumber)) {
            String message = "Cannot transfer money to the same account.";
            Filelog.logError("Transfer failed: " + message);
            throw new SameAccountTransferException(message);
        }

        if (amount <= 0) {
            String message = "Amount must be greater than 0.";
            Filelog.logError(
                    "Transfer failed from [" + fromAccountNumber + "] to [" + toAccountNumber + "]: " + message);
            throw new InvalidAmountException(message);
        }

        if (from.getBalance() < amount) {
            String message = "Insufficient funds in account: " + fromAccountNumber;
            Filelog.logError("Transfer failed from [" + fromAccountNumber + " - " + from.getOwnerName() + "] to ["
                    + toAccountNumber + " - " + to.getOwnerName() + "]: " + message);
            throw new InsufficientFundsException(message);
        }

        from.withdraw(amount); // withdraw đã có log bên trong
        to.deposit(amount); // bạn có thể thêm log trong deposit nếu muốn

        banks.put(from.getAccountNumber(), from);
        banks.put(to.getAccountNumber(), to);

        Filelog.logTransaction(String.format(
                "Transfer $%.2f from [%s - %s] to [%s - %s]",
                amount,
                fromAccountNumber,
                from.getOwnerName(),
                toAccountNumber,
                to.getOwnerName()));
    }
}