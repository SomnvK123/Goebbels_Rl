package CustomeException;// khi rút tiền hoặc chuyển khoản với số tiền lớn hơn số dư

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}