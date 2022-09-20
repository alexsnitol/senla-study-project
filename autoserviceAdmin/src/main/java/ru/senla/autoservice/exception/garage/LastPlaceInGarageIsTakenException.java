package ru.senla.autoservice.exception.garage;

public class LastPlaceInGarageIsTakenException extends Exception {

    public LastPlaceInGarageIsTakenException() {
        super("Last place in the garage is taken");
    }

    public LastPlaceInGarageIsTakenException(String message) {
        super(message);
    }

}
