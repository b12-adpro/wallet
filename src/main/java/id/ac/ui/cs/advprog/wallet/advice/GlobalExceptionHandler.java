package id.ac.ui.cs.advprog.wallet.advice;

import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Menangani NumberFormatException
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<GeneralResponse> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
        GeneralResponse responseBody = GeneralResponse.from(
            null,
            "INVALID_FORMAT",
            "Invalid number format: " + ex.getMessage()
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    // Menangani IllegalArgumentException umum
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        GeneralResponse responseBody = GeneralResponse.from(
            ex.getMessage(), 
            "VALIDATION_ERROR", 
            ex.getMessage() 
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    // Handler untuk exception umum (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> handleGenericException(Exception ex, WebRequest request) {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("An unexpected error occurred: " + ex.getMessage());
        ex.printStackTrace();

        GeneralResponse responseBody = GeneralResponse.from(
            null,
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please try again later."
        );
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}