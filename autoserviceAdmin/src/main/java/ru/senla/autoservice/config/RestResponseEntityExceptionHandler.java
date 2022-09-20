package ru.senla.autoservice.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.senla.autoservice.dto.ExceptionDto;
import ru.senla.autoservice.exception.BusinessRuntimeException;
import ru.senla.autoservice.exception.PropertyAccessDeniedException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> handle(Exception ex, WebRequest request, HttpStatus httpStatus) {
        ExceptionDto exceptionDto = new ExceptionDto(ex.getClass().getSimpleName(), ex.getMessage());
        return handleExceptionInternal(ex, exceptionDto, new HttpHeaders(), httpStatus, request);
    }


    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {PropertyAccessDeniedException.class})
    protected ResponseEntity<Object> handlePropertyAccessDenied(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDenied(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgument(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BusinessRuntimeException.class})
    protected ResponseEntity<Object> handleBusinessRunTime(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleRuntime(RuntimeException ex, WebRequest request) {
        return handle(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleDefault(Exception ex, WebRequest request) {
        return handle(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
