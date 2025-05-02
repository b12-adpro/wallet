package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import java.math.BigDecimal;

@DataJpaTest
@Import(WalletServiceImpl.class)
public class WalletServiceTest {
    
    @Autowired
    private WalletService walletService;
    
    @Test
    public void testGetWalletInfo() {
        Wallet wallet = walletService.getWallet(1L);
        assertNotNull(wallet);
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
    }
    
    @Test
    public void testTopUpWallet() {
        walletService.topUpWallet(1L, "1000");
        Wallet wallet = walletService.getWallet(1L);
        assertEquals(new BigDecimal("1000"), wallet.getBalance());
    }

    @Test
    public void testWithdrawCampaign() {
        walletService.topUpWallet(1L, "1000");
        walletService.withdrawCampaign(1L, "500");
        Wallet wallet = walletService.getWallet(1L);
        assertEquals(new BigDecimal("1500"), wallet.getBalance());
    }

    @Test
    public void testDonateWallet() {
        walletService.topUpWallet(1L, "1000");
        walletService.donateWallet(1L, "300");
        Wallet wallet = walletService.getWallet(1L);
        assertEquals(new BigDecimal("700"), wallet.getBalance());
    }
}