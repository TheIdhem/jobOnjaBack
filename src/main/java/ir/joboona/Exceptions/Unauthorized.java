package ir.joboona.Exceptions;



public class Unauthorized extends RuntimeException{

    public Unauthorized() {
    }

    public Unauthorized(String message) {
        super(message);
    }

}
