package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.observer.NotificationService; 
import id.ac.ui.cs.advprog.wallet.service.WalletService;
import id.ac.ui.cs.advprog.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; 
import org.mockito.Mock; 
import org.mockito.junit.jupiter.MockitoExtension; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryTest {

    @Autowired 
    private WalletRepository walletRepository;

    @Autowired 
    private TransactionRepository transactionRepository;

    @Mock 
    private NotificationService notificationServiceMock;

    private WalletService walletService; 

    @BeforeEach
    void setUp() {
        walletService = new WalletServiceImpl(walletRepository, transactionRepository, notificationServiceMock);
    }

    @Test
    void testAllTransactionTypes() {
        UUID userId = UUID.randomUUID();

        walletService.topUpWallet(userId, "10000");   
        walletService.withdrawCampaign(userId, "5000");  
        walletService.donateWallet(userId, "2000");      

        Wallet wallet = walletService.getWallet(userId);
        assertEquals(0, new BigDecimal("13000").compareTo(wallet.getBalance()));

        List<TransactionEntity> transactions = transactionRepository.findByWalletUserId(userId);
        assertEquals(3, transactions.size());

        boolean topUpFound = false;
        boolean withdrawalFound = false;
        boolean donationFound = false;

        for (TransactionEntity tx : transactions) {
            switch (tx.getType()) {
                case "TOP_UP":
                    topUpFound = true;
                    assertEquals(0, new BigDecimal("10000.00").compareTo(tx.getAmount()));
                    break;
                case "WITHDRAWAL":
                    withdrawalFound = true;
                    assertEquals(0, new BigDecimal("5000.00").compareTo(tx.getAmount()));
                    break;
                case "DONATION":
                    donationFound = true;
                    assertEquals(0, new BigDecimal("2000.00").compareTo(tx.getAmount()));
                    break;
                default:
                    fail("Tipe transaksi tidak dikenal: " + tx.getType());
            }
        }
        assertTrue(topUpFound);
        assertTrue(withdrawalFound);
        assertTrue(donationFound);
    }
}