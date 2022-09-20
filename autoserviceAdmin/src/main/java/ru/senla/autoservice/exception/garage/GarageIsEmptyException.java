package ru.senla.autoservice.exception.garage;

public class GarageIsEmptyException extends Exception {

    public GarageIsEmptyException() {
        super("Number of all place in the garage is 0");
    }

    public GarageIsEmptyException(String message) {
        super(message);
    }
}
