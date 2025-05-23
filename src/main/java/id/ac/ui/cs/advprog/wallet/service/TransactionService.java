package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionEntity> getAllTransactions();
    List<TransactionEntity> getTransactionsByType(String type);
    TransactionEntity getTransactionById(UUID id);
    List<TransactionEntity> getTransactionsByUserId(UUID userId);
    void deleteTopUpTransaction(UUID id);
    void recordTransaction(TransactionEntity transactionEntity);
    BigDecimal getTotalDonationAmount();
    long countDonationTransactions();
    List<TransactionEntity> getAllDonationsByCampaignId(UUID campaignId);
    BigDecimal getTotalDonationsForCampaign(UUID campaignId);
}