package ir.joboona.Exceptions;



public class Forbidden extends RuntimeException{

    public Forbidden() {
    }

    public Forbidden(String message) {
        super(message);
    }

}
