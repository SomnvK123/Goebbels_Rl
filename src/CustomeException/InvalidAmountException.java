package CustomeException;

// khi số tiền giao dịch là số âm hoặc 0
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}