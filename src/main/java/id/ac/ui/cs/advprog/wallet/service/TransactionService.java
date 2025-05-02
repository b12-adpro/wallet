package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionEntity> getAllTransactions();
    List<TransactionEntity> getTransactionsByType(String type);
    TransactionEntity getTransactionById(UUID id);
    void deleteTopUpTransaction(UUID id);
    void recordTransaction(TransactionEntity transactionEntity);
}