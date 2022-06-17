package ru.senla.autoservice.exception;

public class PropertyAccessDeniedException extends Exception {

    public PropertyAccessDeniedException(String comment) {
        super("Access denied" + (!comment.equals("") ? ": " + comment : ""));
    }

}
