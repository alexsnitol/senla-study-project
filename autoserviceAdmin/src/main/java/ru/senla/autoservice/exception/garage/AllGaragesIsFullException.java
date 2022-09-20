package ru.senla.autoservice.exception.garage;

public class AllGaragesIsFullException extends RuntimeException {

    public AllGaragesIsFullException() {
        super("All garages is full");
    }

    public AllGaragesIsFullException(String message) {
        super(message);
    }

}
