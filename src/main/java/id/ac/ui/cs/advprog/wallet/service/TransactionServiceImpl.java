package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<TransactionEntity> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }

    @Override
    public TransactionEntity getTransactionById(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }

    @Override
    public List<TransactionEntity> getTransactionsByUserId(UUID userId) {
        return transactionRepository.findByWalletUserId(userId);
    }

    @Override
    public void deleteTopUpTransaction(UUID id) {
        TransactionEntity transaction = getTransactionById(id);
        if (!"TOP_UP".equalsIgnoreCase(transaction.getType())) {
            throw new RuntimeException("Not a top-up transaction");
        }
        transactionRepository.delete(transaction);
    }

    @Override
    public void recordTransaction(TransactionEntity transactionEntity) {
        transactionRepository.save(transactionEntity);
    }

    @Override
    public BigDecimal getTotalDonationAmount() {
        return transactionRepository.findByType("DONATION").stream()
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long countDonationTransactions() {
        return transactionRepository.findByType("DONATION").size();
    }


    @Override
    public List<TransactionEntity> getAllDonationsByCampaignId(UUID campaignId) {
        if (campaignId == null) {
            throw new IllegalArgumentException("Campaign ID cannot be null.");
        }
        return transactionRepository.findByTypeAndCampaignId("DONATION", campaignId);
    }

    @Override
    public BigDecimal getTotalDonationsForCampaign(UUID campaignId) {
        if (campaignId == null) {
            throw new IllegalArgumentException("Campaign ID cannot be null.");
        }
        BigDecimal total = transactionRepository.sumAmountByTypeAndCampaignId("DONATION", campaignId);
        return total == null ? BigDecimal.ZERO : total;
    }
}