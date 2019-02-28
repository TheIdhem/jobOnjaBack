package Solutions.Core.Exceptions;

public class UnAuthorized extends RuntimeException {
    public UnAuthorized(String message) {
        super(message);
    }
}
