package org.example;

public class BadRequest extends Exception {
    public BadRequest(String message) {
        super(message);
    }
}