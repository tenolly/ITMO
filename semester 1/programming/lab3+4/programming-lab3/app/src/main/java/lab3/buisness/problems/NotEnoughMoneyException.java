package lab3.buisness.problems;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException() {
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
