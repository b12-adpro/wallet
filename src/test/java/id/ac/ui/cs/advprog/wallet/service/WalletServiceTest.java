package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.observer.NotificationService;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import id.ac.ui.cs.advprog.wallet.repository.WalletRepository;
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
public class WalletServiceTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationServiceMock;

    private WalletService walletService;
    private UUID generatedUuid;
    private UUID testCampaignId;
    private UUID testDonationId; 

    @BeforeEach
    void setUp() {
        walletService = new WalletServiceImpl(walletRepository, transactionRepository, notificationServiceMock);
        generatedUuid = UUID.randomUUID();
        testCampaignId = UUID.randomUUID();
        testDonationId = UUID.randomUUID();
    }

    @Test
    void testGetWalletInfo() {
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertNotNull(wallet);
        assertEquals(generatedUuid, wallet.getUserId());
        assertEquals(0, BigDecimal.ZERO.compareTo(wallet.getBalance()));
    }

    @Test
    void testTopUpWallet_shouldCreateTransaction() {
        walletService.topUpWallet(generatedUuid, "10000");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(0, new BigDecimal("10000").compareTo(wallet.getBalance()));

        List<TransactionEntity> userTransactions = transactionRepository.findByWalletUserId(generatedUuid);
        TransactionEntity topUpTx = userTransactions.stream()
                .filter(tx -> "TOP_UP".equals(tx.getType()))
                .findFirst()
                .orElse(null);

        assertNotNull(topUpTx);
        assertEquals("TOP_UP", topUpTx.getType());
        assertEquals(0, new BigDecimal("10000").compareTo(topUpTx.getAmount()));
        assertNull(topUpTx.getCampaignId());
        assertNull(topUpTx.getDonationId()); 
    }

    @Test
    void testWithdrawCampaign_shouldIncreaseBalanceAndSaveCampaignId() {
        walletService.topUpWallet(generatedUuid, "10000");
        walletService.withdrawCampaign(generatedUuid, "5000", testCampaignId);
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(0, new BigDecimal("15000").compareTo(wallet.getBalance()));

        List<TransactionEntity> userTransactions = transactionRepository.findByWalletUserId(generatedUuid);
        TransactionEntity withdrawalTx = userTransactions.stream()
                .filter(tx -> "WITHDRAWAL".equals(tx.getType()))
                .findFirst()
                .orElse(null);

        assertNotNull(withdrawalTx);
        assertEquals("WITHDRAWAL", withdrawalTx.getType());
        assertEquals(0, new BigDecimal("5000").compareTo(withdrawalTx.getAmount()));
        assertEquals(testCampaignId, withdrawalTx.getCampaignId());
        assertNull(withdrawalTx.getDonationId()); 
    }

    @Test
    void testDonateWallet_shouldDecreaseBalanceAndSaveCampaignAndDonationId() {
        walletService.topUpWallet(generatedUuid, "10000");
        walletService.donateWallet(generatedUuid, "3000", testCampaignId, testDonationId);
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(0, new BigDecimal("7000").compareTo(wallet.getBalance()));

        List<TransactionEntity> userTransactions = transactionRepository.findByWalletUserId(generatedUuid);
        TransactionEntity donationTx = userTransactions.stream()
                .filter(tx -> "DONATION".equals(tx.getType()))
                .findFirst()
                .orElse(null);

        assertNotNull(donationTx);
        assertEquals("DONATION", donationTx.getType());
        assertEquals(0, new BigDecimal("3000").compareTo(donationTx.getAmount()));
        assertEquals(testCampaignId, donationTx.getCampaignId());
        assertEquals(testDonationId, donationTx.getDonationId()); 
    }

    @Test
    void testDonateWallet_insufficientBalance_shouldThrowException() {
        walletService.topUpWallet(generatedUuid, "20000");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            walletService.donateWallet(generatedUuid, "30000", testCampaignId, testDonationId);
        });
        assertTrue(exception.getMessage().contains("Insufficient balance for donation"));
    }

    @Test
    void testDonateWallet_nonPositiveAmount_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            walletService.donateWallet(generatedUuid, "0", testCampaignId, testDonationId);
        });
        assertEquals("Donation amount must be positive.", exception.getMessage());
    }
    
    @Test
    void testDonateWallet_nullCampaignIdOrDonationId_shouldThrowException() {
        IllegalArgumentException exCampaignNull = assertThrows(IllegalArgumentException.class, () -> {
            walletService.donateWallet(generatedUuid, "1000", null, testDonationId);
        });
        assertEquals("CampaignId and DonationId are required for donation.", exCampaignNull.getMessage());

        IllegalArgumentException exDonationNull = assertThrows(IllegalArgumentException.class, () -> {
            walletService.donateWallet(generatedUuid, "1000", testCampaignId, null);
        });
        assertEquals("CampaignId and DonationId are required for donation.", exDonationNull.getMessage());
    }
}