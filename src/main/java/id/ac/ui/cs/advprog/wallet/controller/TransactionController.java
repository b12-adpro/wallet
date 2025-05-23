package id.ac.ui.cs.advprog.wallet.controller;

import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @GetMapping("")
    public ResponseEntity<GeneralResponse> getAllTransactions() {
        List<TransactionEntity> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(GeneralResponse.from(transactions, "OK", "Success"), HttpStatus.OK);
    }
    
    @GetMapping("/type")
    public ResponseEntity<GeneralResponse> getTransactionsByType(@RequestParam("type") String type) {
        List<TransactionEntity> transactions = transactionService.getTransactionsByType(type);
        return new ResponseEntity<>(GeneralResponse.from(transactions, "OK", "Success"), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getTransaction(@PathVariable("id") UUID id) {
        TransactionEntity transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(GeneralResponse.from(transaction, "OK", "Success"), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteTopUpTransaction(@PathVariable("id") UUID id) {
        transactionService.deleteTopUpTransaction(id);
        return new ResponseEntity<>(GeneralResponse.from(null, "OK", "Transaction deleted"), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> getTransactionsByUser(@PathVariable("userId") UUID userId) {
        List<TransactionEntity> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(GeneralResponse.from(transactions, "OK", "Success"), HttpStatus.OK);
    }

    @GetMapping("/donations/campaign/{campaignId}")
    public ResponseEntity<GeneralResponse> getAllDonationsByCampaignId(@PathVariable("campaignId") UUID campaignId) {
        List<TransactionEntity> transactions = transactionService.getAllDonationsByCampaignId(campaignId);
        return new ResponseEntity<>(GeneralResponse.from(transactions, "OK", "Donation transactions for campaign retrieved successfully."), HttpStatus.OK);
    }

    @GetMapping("/campaign/{campaignId}/donations/total")
    public ResponseEntity<GeneralResponse> getTotalDonationsForCampaign(@PathVariable("campaignId") UUID campaignId) {
        BigDecimal totalDonations = transactionService.getTotalDonationsForCampaign(campaignId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaignId", campaignId);
        responseData.put("totalDonationAmount", totalDonations);

        return new ResponseEntity<>(GeneralResponse.from(responseData, "OK", "Total donations for campaign retrieved successfully."), HttpStatus.OK);
    }
}