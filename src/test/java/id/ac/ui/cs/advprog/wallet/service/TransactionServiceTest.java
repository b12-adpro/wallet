package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TransactionServiceImpl.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    // wallet diabaikan dengan null untuk penyederhanaan
    private TransactionEntity createTransactionEntity(String type, BigDecimal amount) {
        TransactionEntity entity = new TransactionEntity(type, amount, LocalDateTime.now(), null);
        return transactionRepository.save(entity);
    }
    
    @Test
    public void testGetAllTransactions() {
        createTransactionEntity("TOP_UP", new BigDecimal("1000"));
        createTransactionEntity("WITHDRAWAL", new BigDecimal("500"));
        
        List<TransactionEntity> transactions = transactionService.getAllTransactions();
        
        assertEquals(2, transactions.size());
    }
    
    @Test
    public void testGetTransactionsByType() {
        createTransactionEntity("TOP_UP", new BigDecimal("1000"));
        createTransactionEntity("WITHDRAWAL", new BigDecimal("500"));
        createTransactionEntity("TOP_UP", new BigDecimal("2000"));
        
        List<TransactionEntity> topUpTransactions = transactionService.getTransactionsByType("TOP_UP");
        assertEquals(2, topUpTransactions.size());
        
        List<TransactionEntity> withdrawalTransactions = transactionService.getTransactionsByType("WITHDRAWAL");
        assertEquals(1, withdrawalTransactions.size());
    }
    
    @Test
    public void testGetTransactionById() {
        TransactionEntity entity = createTransactionEntity("DONATION", new BigDecimal("300"));
        TransactionEntity retrieved = transactionService.getTransactionById(entity.getId());
        assertNotNull(retrieved);
        assertEquals("DONATION", retrieved.getType());
    }
    
    @Test
    public void testDeleteTopUpTransaction() {
        TransactionEntity topUpEntity = createTransactionEntity("TOP_UP", new BigDecimal("1000"));
        transactionService.deleteTopUpTransaction(topUpEntity.getId());
        
        assertFalse(transactionRepository.findById(topUpEntity.getId()).isPresent());
    }
}