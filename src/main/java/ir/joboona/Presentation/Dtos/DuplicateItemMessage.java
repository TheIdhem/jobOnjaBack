package ir.joboona.Presentation.Dtos;

public class DuplicateItemMessage {

    public DuplicateItemMessage() {
    }

    public DuplicateItemMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
