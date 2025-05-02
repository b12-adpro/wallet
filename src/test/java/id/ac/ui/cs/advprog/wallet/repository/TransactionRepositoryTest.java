package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.service.WalletService;
import id.ac.ui.cs.advprog.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(WalletServiceImpl.class)
public class TransactionRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private WalletService walletService;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Test
    public void testSaveWithdrawalTransaction() {
        walletService.topUpWallet(1L, "1000");
        walletService.withdrawWallet(1L, "500");
        
        List<TransactionEntity> transactions = transactionRepository.findByWalletUserId(1L);
        assertFalse(transactions.isEmpty());
        TransactionEntity trx = transactions.get(0);
        assertEquals("WITHDRAWAL", trx.getType());
        assertEquals(new BigDecimal("500"), trx.getAmount());
    }
}
