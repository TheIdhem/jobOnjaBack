package Solutions.Core.Exceptions;

public class BadRequest extends RuntimeException {

    private String missingParams;

    public BadRequest(String missingParams) {
        this.missingParams = missingParams;
    }

    @Override
    public String getMessage() {
        return "Missing param: " + missingParams;
    }
}
