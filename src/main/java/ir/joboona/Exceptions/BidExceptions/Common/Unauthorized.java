package ir.joboona.Exceptions.BidExceptions.Common;

public class Unauthorized extends RuntimeException{

    public Unauthorized() {
    }

    public Unauthorized(String message) {
        super(message);
    }
}
