package Enum;

public enum Action {
    ADD_BANK_ACCOUNT(1, "Add Bank Account"),
    UPDATE_BANK_ACCOUNT(2, "Update Bank Account"),
    DELETE_BANK_ACCOUNT(3, "Delete Bank Account"),
    LIST_BANK_ACCOUNTS(4, "List Bank Accounts"),
    DEPOSIT_MONEY(5, "Deposit Money"),
    WITHDRAW_MONEY(6, "Withdraw Money"),
    EXIT(0, "Exit"),
    TRANSFER_MONEY(7, "Transfer Money" );

    private final int value;
    private final String description;

    //constructor
    Action(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Action getAction(int value) {
        Action[] actions = Action.values();
        for (Action action : actions) {
            if (action.getValue() == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid action id: " + value);
    }
}
