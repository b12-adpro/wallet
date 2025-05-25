package id.ac.ui.cs.advprog.wallet.advice;

import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<GeneralResponse> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
        if (logger.isWarnEnabled()) { 
            logger.warn("Invalid number format for request {}: {}", request.getDescription(false), ex.getMessage());
        }

        GeneralResponse responseBody = GeneralResponse.from(
            null,
            "INVALID_FORMAT",
            "Invalid number format: " + ex.getMessage() 
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        if (logger.isWarnEnabled()) { 
            logger.warn("Validation error for request {}: {}", request.getDescription(false), ex.getMessage());
        }

        GeneralResponse responseBody = GeneralResponse.from(
            ex.getMessage(),
            "VALIDATION_ERROR",
            ex.getMessage()
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> handleGenericException(Exception ex, WebRequest request) {
        if (logger.isErrorEnabled()) {
             logger.error("An unexpected error occurred while processing request {}:", request.getDescription(false), ex);
        }


        GeneralResponse responseBody = GeneralResponse.from(
            null,
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please try again later."
        );
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}