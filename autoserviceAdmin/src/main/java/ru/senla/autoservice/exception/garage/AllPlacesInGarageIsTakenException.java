package ru.senla.autoservice.exception.garage;

public class AllPlacesInGarageIsTakenException extends Exception {

    public AllPlacesInGarageIsTakenException() {
        super("All place in the garage is taken");
    }

    public AllPlacesInGarageIsTakenException(String message) {
        super(message);
    }

}
