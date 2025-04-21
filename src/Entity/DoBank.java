package Entity;

import Exception.*;

public interface DoBank {
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount) throws InsufficientFundsException, InvalidAmountException ;
    double getBalance();
}