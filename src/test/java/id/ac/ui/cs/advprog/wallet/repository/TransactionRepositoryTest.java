package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.service.WalletService;
import id.ac.ui.cs.advprog.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class) 
class TransactionRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private WalletService walletService;

    private UUID userId1;
    private UUID campaignId1;
    private UUID campaignId2;
    private UUID donationId1; 
    private UUID donationId2;

    @BeforeEach
    void setUp() {
        walletService = new WalletServiceImpl(walletRepository, transactionRepository);

        userId1 = UUID.randomUUID();
        campaignId1 = UUID.randomUUID();
        campaignId2 = UUID.randomUUID();
        donationId1 = UUID.randomUUID();
        donationId2 = UUID.randomUUID();

        walletService.topUpWallet(userId1, "50000");
    }

    @Test
    void testAllTransactionTypes_withCampaignAndDonationIds() {
        walletService.topUpWallet(userId1, "10000");
        walletService.withdrawCampaign(userId1, "5000", campaignId1);
        walletService.donateWallet(userId1, "2000", campaignId1, donationId1);

        Wallet wallet = walletService.getWallet(userId1);
        assertEquals(0, new BigDecimal("63000").compareTo(wallet.getBalance()));

        List<TransactionEntity> transactions = transactionRepository.findByWalletUserId(userId1);
        assertEquals(4, transactions.size());

        TransactionEntity topUpTx = transactions.stream().filter(tx -> "TOP_UP".equals(tx.getType()) && tx.getAmount().compareTo(new BigDecimal("10000.00")) == 0).findFirst().orElse(null);
        TransactionEntity withdrawalTx = transactions.stream().filter(tx -> "WITHDRAWAL".equals(tx.getType())).findFirst().orElse(null);
        TransactionEntity donationTx = transactions.stream().filter(tx -> "DONATION".equals(tx.getType())).findFirst().orElse(null);

        assertNotNull(topUpTx);
        assertNull(topUpTx.getCampaignId());
        assertNull(topUpTx.getDonationId());

        assertNotNull(withdrawalTx);
        assertEquals(campaignId1, withdrawalTx.getCampaignId());
        assertNull(withdrawalTx.getDonationId());

        assertNotNull(donationTx);
        assertEquals(campaignId1, donationTx.getCampaignId());
        assertEquals(donationId1, donationTx.getDonationId()); 
    }

    @Test
    void testFindByTypeAndCampaignId_forDonations() {
        walletService.donateWallet(userId1, "1000", campaignId1, donationId1);
        walletService.donateWallet(userId1, "1500", campaignId1, donationId2);
        walletService.donateWallet(userId1, "500", campaignId2, donationId1);
        walletService.withdrawCampaign(userId1, "200", campaignId1);

        List<TransactionEntity> donationsForCampaign1 = transactionRepository.findByTypeAndCampaignId("DONATION", campaignId1);
        assertEquals(2, donationsForCampaign1.size());
    }

    @Test
    void testSumAmountByTypeAndCampaignId_forDonations() {
        walletService.donateWallet(userId1, "1000", campaignId1, donationId1);
        walletService.donateWallet(userId1, "2500", campaignId1, donationId2);

        BigDecimal totalDonationsCampaign1 = transactionRepository.sumAmountByTypeAndCampaignId("DONATION", campaignId1);
        assertNotNull(totalDonationsCampaign1);
        assertEquals(0, new BigDecimal("3500.00").compareTo(totalDonationsCampaign1));
    }
}