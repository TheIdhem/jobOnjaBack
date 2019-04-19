package Solutions.Core.Exceptions;

public class IllegalFormat extends RuntimeException {

    public IllegalFormat() {
    }

    public IllegalFormat(String message) {
        super(message);
    }
}
