package ir.joboona.Exceptions;

import com.fasterxml.jackson.annotation.JsonValue;

public class Unauthorized extends RuntimeException{

    public Unauthorized() {
    }

    public Unauthorized(String message) {
        super(message);
    }
}
