package com.example.appointment_scheduler.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.appointment_scheduler.model.ErrorMessage;


@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler 
    extends ResponseEntityExceptionHandler {
    
        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException ex, WebRequest request) {
            ErrorMessage message 
                = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorMessage> userAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
            ErrorMessage message 
                = new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        @ExceptionHandler(IncorrectPasswordException.class)
        public ResponseEntity<ErrorMessage> incorrectPasswordException(IncorrectPasswordException ex, WebRequest request) {
            ErrorMessage message 
                = new ErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }

        @ExceptionHandler(AppointmentNotFoundException.class)
        public ResponseEntity<ErrorMessage> appointmentNotFoundException(AppointmentNotFoundException ex, WebRequest request) {
            ErrorMessage message 
                = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());      
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);

        }

        @ExceptionHandler(AppointmentAlreadyBooked.class)
        public ResponseEntity<ErrorMessage> appointmentAlreadyBookedException(AppointmentAlreadyBooked ex, WebRequest request) {
            ErrorMessage message 
                = new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage());      
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);

        }
}
