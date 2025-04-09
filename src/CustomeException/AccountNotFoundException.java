package CustomeException;

//khi tìm kiếm hoặc chuyển tiền đến tài khoản không tồn tại
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}