package id.ac.ui.cs.advprog.wallet.controller;

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
import java.util.List;
import java.util.Map;     
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController; 

    @Test
    void testGetAllTransactions() {
        TransactionEntity trx1 = new TransactionEntity("TOP_UP", new BigDecimal("1000"), LocalDateTime.now(), null, null, null);
        TransactionEntity trx2 = new TransactionEntity("WITHDRAWAL", new BigDecimal("500"), LocalDateTime.now(), null, UUID.randomUUID(), null);
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(trx1, trx2));

        ResponseEntity<GeneralResponse> response = transactionController.getAllTransactions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Success", body.getMessage());
        assertEquals(((List<?>) body.getData()).size(), 2);
    }

    @Test
    void testGetTransactionsByType() {
        UUID campaignId = UUID.randomUUID();
        TransactionEntity trx = new TransactionEntity("WITHDRAWAL", new BigDecimal("500"), LocalDateTime.now(), null, campaignId, null);
        when(transactionService.getTransactionsByType("WITHDRAWAL")).thenReturn(Arrays.asList(trx));

        ResponseEntity<GeneralResponse> response = transactionController.getTransactionsByType("WITHDRAWAL");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        TransactionEntity returned = ((java.util.List<TransactionEntity>) body.getData()).get(0);
        assertEquals("WITHDRAWAL", returned.getType());
    }

    @Test
    void testGetSpecificTransaction() {
        UUID sampleId = UUID.randomUUID();
        TransactionEntity trx = new TransactionEntity("TOP_UP", new BigDecimal("1000"), LocalDateTime.now(), null, null, null);
        trx.setId(sampleId); 
        when(transactionService.getTransactionById(sampleId)).thenReturn(trx);

        ResponseEntity<GeneralResponse> response = transactionController.getTransaction(sampleId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        TransactionEntity returned = (TransactionEntity) body.getData();
        assertEquals("TOP_UP", returned.getType());
        assertEquals(0, new BigDecimal("1000").compareTo(returned.getAmount()));
        assertEquals(sampleId, returned.getId());
    }

    @Test
    void testDeleteTopUpTransaction() {
        UUID sampleId = UUID.randomUUID();
        doNothing().when(transactionService).deleteTopUpTransaction(sampleId);

        ResponseEntity<GeneralResponse> response = transactionController.deleteTopUpTransaction(sampleId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Transaction deleted", body.getMessage());
    }

    @Test
    void testGetTransactionsByUser() {
        UUID userId = UUID.randomUUID();
        TransactionEntity trx = new TransactionEntity("TOP_UP", new BigDecimal("100"), LocalDateTime.now(), null, null, null);
        when(transactionService.getTransactionsByUserId(userId)).thenReturn(Arrays.asList(trx));

        ResponseEntity<GeneralResponse> response = transactionController.getTransactionsByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        List<TransactionEntity> transactions = (List<TransactionEntity>) body.getData();
        assertEquals(1, transactions.size());
        assertEquals("TOP_UP", transactions.get(0).getType());
    }

    @Test
    void testGetTotalDonationsForCampaign() {
        UUID campaignId = UUID.randomUUID();
        BigDecimal expectedTotal = new BigDecimal("12345.67");
        when(transactionService.getTotalDonationsForCampaign(campaignId)).thenReturn(expectedTotal);

        ResponseEntity<GeneralResponse> response = transactionController.getTotalDonationsForCampaign(campaignId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Total donations for campaign retrieved successfully.", body.getMessage());
        Map<String, Object> responseData = (Map<String, Object>) body.getData();
        assertEquals(campaignId, responseData.get("campaignId"));
        assertEquals(0, expectedTotal.compareTo(new BigDecimal(responseData.get("totalDonationAmount").toString())));
    }

    @Test
    void testGetAllDonationsByCampaignId() {
        UUID campaignId = UUID.randomUUID();
        UUID donationIdDummy = UUID.randomUUID();
        TransactionEntity donation1 = new TransactionEntity("DONATION", new BigDecimal("100"), LocalDateTime.now(), null, campaignId, donationIdDummy);
        when(transactionService.getAllDonationsByCampaignId(campaignId)).thenReturn(Arrays.asList(donation1));

        ResponseEntity<GeneralResponse> response = transactionController.getAllDonationsByCampaignId(campaignId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        List<TransactionEntity> data = (List<TransactionEntity>) body.getData();
        assertEquals(1, data.size());
        assertEquals("DONATION", data.get(0).getType());
        assertEquals(campaignId, data.get(0).getCampaignId());
    }
}