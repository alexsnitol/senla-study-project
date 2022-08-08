package ru.senla.autoservice.exception.order;

public class TimeOfBeginInOrderNotSetException extends Exception {

    public TimeOfBeginInOrderNotSetException() {
        super("Time of begin in order not set");
    }

    public TimeOfBeginInOrderNotSetException(String message) {
        super(message);
    }

}
