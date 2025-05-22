package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.observer.NotificationService;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import id.ac.ui.cs.advprog.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Penting untuk @Mock
import org.mockito.Mock; // Penting
import org.mockito.junit.jupiter.MockitoExtension; // Penting
import org.springframework.beans.factory.annotation.Autowired; // HANYA untuk repository
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// Tidak perlu @TestConfiguration atau @Bean jika semua dibuat manual

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
// Tidak perlu mock() static import jika pakai @Mock

@DataJpaTest
@ExtendWith(MockitoExtension.class) // Aktifkan Mockito untuk @Mock
public class WalletServiceTest {

    // @TestConfiguration dihapus

    @Autowired // Repository tetap di-inject oleh @DataJpaTest
    private WalletRepository walletRepository;

    @Autowired // Repository tetap di-inject oleh @DataJpaTest
    private TransactionRepository transactionRepository;

    @Mock // Buat mock NotificationService menggunakan anotasi
    private NotificationService notificationServiceMock;
    // Tidak ada @Autowired untuk notificationService di sini

    private WalletService walletService; // Akan kita inisialisasi manual
    private UUID generatedUuid;

    @BeforeEach
    void setUp() {
        // Buat instance WalletServiceImpl secara manual dengan repository asli dan mock NotificationService
        walletService = new WalletServiceImpl(walletRepository, transactionRepository, notificationServiceMock);
        generatedUuid = UUID.randomUUID();
    }

    // ... (Tes-tes tetap sama) ...
    @Test
    void testGetWalletInfo() {
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertNotNull(wallet);
        assertEquals(generatedUuid, wallet.getUserId());
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
    }

    @Test
    void testTopUpWallet() {
        walletService.topUpWallet(generatedUuid, "10000");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(0, new BigDecimal("10000").compareTo(wallet.getBalance()));
    }

    @Test
    void testWithdrawCampaign() {
        walletService.topUpWallet(generatedUuid, "10000");
        walletService.withdrawCampaign(generatedUuid, "5000");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(0, new BigDecimal("15000").compareTo(wallet.getBalance()));
    }

    @Test
    void testDonateWallet() {
        walletService.topUpWallet(generatedUuid, "10000");
        walletService.donateWallet(generatedUuid, "3000");
        Wallet wallet = walletService.getWallet(generatedUuid);
        assertEquals(0, new BigDecimal("7000").compareTo(wallet.getBalance()));
    }
}