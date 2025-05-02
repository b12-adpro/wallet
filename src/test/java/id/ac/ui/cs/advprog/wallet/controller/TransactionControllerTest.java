package id.ac.ui.cs.advprog.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;;

    @Test
    public void testGetAllTransactions() {
        TransactionEntity trx1 = new TransactionEntity("TOP_UP", new BigDecimal("1000"), LocalDateTime.now(), null);
        TransactionEntity trx2 = new TransactionEntity("WITHDRAWAL", new BigDecimal("500"), LocalDateTime.now(), null);
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(trx1, trx2));

        ResponseEntity<GeneralResponse> response = transactionController.getAllTransactions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("OK", body.getStatus());
        assertEquals("Success", body.getMessage());
    }

    @Test
    public void testGetTransactionsByType() {
        TransactionEntity trx = new TransactionEntity("WITHDRAWAL", new BigDecimal("500"), LocalDateTime.now(), null);
        when(transactionService.getTransactionsByType("WITHDRAWAL")).thenReturn(Arrays.asList(trx));

        ResponseEntity<GeneralResponse> response = transactionController.getTransactionsByType("WITHDRAWAL");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("OK", body.getStatus());
        TransactionEntity returned = ((java.util.List<TransactionEntity>) body.getData()).get(0);
        assertEquals("WITHDRAWAL", returned.getType());
    }

    @Test
    public void testGetSpecificTransaction() {
        UUID sampleId = UUID.randomUUID();
        TransactionEntity trx = new TransactionEntity("TOP_UP", new BigDecimal("1000"), LocalDateTime.now(), null);
        sampleId = trx.getId();
        when(transactionService.getTransactionById(sampleId)).thenReturn(trx);

        ResponseEntity<GeneralResponse> response = transactionController.getTransaction(sampleId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        TransactionEntity returned = (TransactionEntity) body.getData();
        assertEquals("TOP_UP", returned.getType());
        assertEquals(new BigDecimal("1000"), returned.getAmount());
    }

    @Test
    public void testDeleteTopUpTransaction() {
        UUID sampleId = UUID.randomUUID();
        doNothing().when(transactionService).deleteTopUpTransaction(sampleId);

        ResponseEntity<GeneralResponse> response = transactionController.deleteTopUpTransaction(sampleId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("OK", body.getStatus());
        assertEquals("Transaction deleted", body.getMessage());
    }
}