package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
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
}