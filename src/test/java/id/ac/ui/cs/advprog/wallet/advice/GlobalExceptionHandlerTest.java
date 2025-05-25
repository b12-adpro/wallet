package id.ac.ui.cs.advprog.wallet.advice;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest mockWebRequest;

    private ListAppender<ILoggingEvent> listAppender;
    private ch.qos.logback.classic.Logger appLogger; 

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();

        lenient().when(mockWebRequest.getDescription(false)).thenReturn("test-request-uri");

        appLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        appLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        appLogger.detachAppender(listAppender);
        listAppender.stop();
    }

    @Test
    void handleNumberFormatException_whenWarnIsEnabled_logsAndReturnsCorrectResponse() {
        appLogger.setLevel(Level.WARN);
        NumberFormatException ex = new NumberFormatException("For input string: \"invalid\"");
        String expectedLogMessage = "Invalid number format for request test-request-uri: For input string: \"invalid\"";

        ResponseEntity<GeneralResponse> responseEntity = globalExceptionHandler.handleNumberFormatException(ex, mockWebRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        GeneralResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("INVALID_FORMAT", responseBody.getStatus());
        assertEquals("Invalid number format: For input string: \"invalid\"", responseBody.getMessage());
        assertNull(responseBody.getData());

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size());
        ILoggingEvent loggingEvent = logsList.get(0);
        assertEquals(Level.WARN, loggingEvent.getLevel());
        assertTrue(loggingEvent.getFormattedMessage().contains(expectedLogMessage));
        verify(mockWebRequest, times(1)).getDescription(false); // Diverifikasi karena logger.isWarnEnabled() true
    }

    @Test
    void handleNumberFormatException_whenWarnIsDisabled_returnsCorrectResponseWithoutLoggingWarn() {
        appLogger.setLevel(Level.ERROR); // Set level lebih tinggi dari WARN
        NumberFormatException ex = new NumberFormatException("For input string: \"nodisplay\"");

        ResponseEntity<GeneralResponse> responseEntity = globalExceptionHandler.handleNumberFormatException(ex, mockWebRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        GeneralResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("INVALID_FORMAT", responseBody.getStatus());
        assertEquals("Invalid number format: For input string: \"nodisplay\"", responseBody.getMessage());
        assertNull(responseBody.getData());


        List<ILoggingEvent> logsList = listAppender.list;
        assertTrue(logsList.stream().noneMatch(event -> event.getLevel() == Level.WARN && event.getLoggerName().equals(GlobalExceptionHandler.class.getName())));
        // getDescription TIDAK dipanggil karena logger.isWarnEnabled() false
        verify(mockWebRequest, times(0)).getDescription(false);
    }

    @Test
    void handleIllegalArgumentException_whenWarnIsEnabled_logsAndReturnsCorrectResponse() {
        appLogger.setLevel(Level.WARN);
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument provided");
        String expectedLogMessage = "Validation error for request test-request-uri: Illegal argument provided";

        ResponseEntity<GeneralResponse> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex, mockWebRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        GeneralResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("VALIDATION_ERROR", responseBody.getStatus());
        assertEquals("Illegal argument provided", responseBody.getMessage());
        assertEquals("Illegal argument provided", responseBody.getData());

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size());
        ILoggingEvent loggingEvent = logsList.get(0);
        assertEquals(Level.WARN, loggingEvent.getLevel());
        assertTrue(loggingEvent.getFormattedMessage().contains(expectedLogMessage));
        verify(mockWebRequest, times(1)).getDescription(false); 
    }

    @Test
    void handleIllegalArgumentException_whenWarnIsDisabled_returnsCorrectResponseWithoutLoggingWarn() {
        appLogger.setLevel(Level.ERROR);
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument provided");

        ResponseEntity<GeneralResponse> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex, mockWebRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        GeneralResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("VALIDATION_ERROR", responseBody.getStatus());
        assertEquals("Illegal argument provided", responseBody.getMessage());
        assertEquals("Illegal argument provided", responseBody.getData());


        List<ILoggingEvent> logsList = listAppender.list;
        assertTrue(logsList.stream().noneMatch(event -> event.getLevel() == Level.WARN && event.getLoggerName().equals(GlobalExceptionHandler.class.getName())));
        verify(mockWebRequest, times(0)).getDescription(false); 
    }


    @Test
    void handleGenericException_whenErrorIsEnabled_logsAndReturnsCorrectResponse() {
        appLogger.setLevel(Level.ERROR);
        RuntimeException ex = new RuntimeException("A wild unexpected error appears!");
        String expectedLogMessage = "An unexpected error occurred while processing request test-request-uri:";

        ResponseEntity<GeneralResponse> responseEntity = globalExceptionHandler.handleGenericException(ex, mockWebRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        GeneralResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("INTERNAL_SERVER_ERROR", responseBody.getStatus());
        assertEquals("An unexpected error occurred. Please try again later.", responseBody.getMessage());
        assertNull(responseBody.getData());

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size());
        ILoggingEvent loggingEvent = logsList.get(0);
        assertEquals(Level.ERROR, loggingEvent.getLevel());
        assertTrue(loggingEvent.getFormattedMessage().contains(expectedLogMessage));
        assertNotNull(loggingEvent.getThrowableProxy());
        assertEquals(ex.getClass().getName(), loggingEvent.getThrowableProxy().getClassName());
        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    void handleGenericException_whenErrorIsDisabled_returnsCorrectResponseWithoutLoggingError() {
        appLogger.setLevel(Level.OFF);
        RuntimeException ex = new RuntimeException("A wild unexpected error appears!");

        ResponseEntity<GeneralResponse> responseEntity = globalExceptionHandler.handleGenericException(ex, mockWebRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        GeneralResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("INTERNAL_SERVER_ERROR", responseBody.getStatus());
        assertEquals("An unexpected error occurred. Please try again later.", responseBody.getMessage());
        assertNull(responseBody.getData());

        List<ILoggingEvent> logsList = listAppender.list;
        assertTrue(logsList.stream().noneMatch(event -> event.getLevel() == Level.ERROR && event.getLoggerName().equals(GlobalExceptionHandler.class.getName())));
        verify(mockWebRequest, times(0)).getDescription(false); 
    }
}