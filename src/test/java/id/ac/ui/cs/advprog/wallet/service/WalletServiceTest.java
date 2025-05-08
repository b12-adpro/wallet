package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import java.math.BigDecimal;
import java.util.UUID;

@DataJpaTest
@Import(WalletServiceImpl.class)
public class WalletServiceTest {
    
    @Autowired
    private WalletService walletService;
    UUID generatedUuid = UUID.randomUUID();
    
    @Test
    public void testGetWalletInfo() {
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertNotNull(wallet);
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
    }
    
    @Test
    public void testTopUpWallet() {
        walletService.topUpWallet(generatedUuid, "1000");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(new BigDecimal("1000"), wallet.getBalance());
    }

    @Test
    public void testWithdrawCampaign() {
        walletService.topUpWallet(generatedUuid, "1000");
        walletService.withdrawCampaign(generatedUuid, "500");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(new BigDecimal("1500"), wallet.getBalance());
    }

    @Test
    public void testDonateWallet() {
        walletService.topUpWallet(generatedUuid, "1000");
        walletService.donateWallet(generatedUuid, "300");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(new BigDecimal("700"), wallet.getBalance());
    }
}