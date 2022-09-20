package ru.senla.autoservice.exception.garage;

public class PlaceInGarageIsTakenException extends Exception {

    public PlaceInGarageIsTakenException() {
        super("Place in the garage is taken");
    }

    public PlaceInGarageIsTakenException(String message) {
        super(message);
    }
}
