package ir.joboona.Presentation.Dtos;

public class UnauthorizedDto {

    public UnauthorizedDto() {
    }

    public UnauthorizedDto(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
