package Solutions.Core.Exceptions;

public class MissingParameter extends RuntimeException {

    private String missingParam;

    public MissingParameter() {
    }

    public MissingParameter(String missingParam) {
        this.missingParam = missingParam;
    }

    @Override
    public String getMessage() {
        return "Missing param: " + missingParam;
    }
}
