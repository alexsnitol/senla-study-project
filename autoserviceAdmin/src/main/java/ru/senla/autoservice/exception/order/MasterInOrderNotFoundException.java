package ru.senla.autoservice.exception.order;

public class MasterInOrderNotFoundException extends Exception {

    public MasterInOrderNotFoundException() {
        super("Master in order not found");
    }

    public MasterInOrderNotFoundException(String message) {
        super(message);
    }
}
