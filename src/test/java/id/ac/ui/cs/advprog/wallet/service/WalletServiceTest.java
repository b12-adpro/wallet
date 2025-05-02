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
    private WalletRepository walletRepository;
    
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
    public void testWithdrawWallet() {
        walletService.topUpWallet(1L, "1000");
        walletService.withdrawWallet(1L, "500");
        Wallet wallet = walletService.getWallet(1L);
        assertEquals(new BigDecimal("500"), wallet.getBalance());
    }
}