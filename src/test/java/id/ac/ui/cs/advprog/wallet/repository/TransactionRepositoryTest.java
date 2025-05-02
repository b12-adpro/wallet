package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
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
    private WalletService walletService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testAllTransactionTypes() {
        Long userId = 1L;

        walletService.topUpWallet(userId, "1000");
        walletService.withdrawCampaign(userId, "500");
        walletService.donateWallet(userId, "200");

        Wallet wallet = walletService.getWallet(userId);
        assertEquals(new BigDecimal("1300"), wallet.getBalance(), "Saldo akhir harus 1300");

        List<TransactionEntity> transactions = transactionRepository.findByWalletUserId(userId);
        assertEquals(3, transactions.size(), "Harus tercatat 3 transaksi");

        boolean topUpFound = false;
        boolean withdrawalFound = false;
        boolean donationFound = false;

        for (TransactionEntity tx : transactions) {
            switch (tx.getType()) {
                case "TOP_UP":
                    topUpFound = true;
                    assertEquals(new BigDecimal("1000"), tx.getAmount(), "Jumlah top-up harus 1000");
                    break;
                case "WITHDRAWAL":
                    withdrawalFound = true;
                    assertEquals(new BigDecimal("500"), tx.getAmount(), "Jumlah withdrawal harus 500");
                    break;
                case "DONATION":
                    donationFound = true;
                    assertEquals(new BigDecimal("200"), tx.getAmount(), "Jumlah donation harus 200");
                    break;
                default:
                    fail("Tipe transaksi tidak dikenal: " + tx.getType());
            }
        }

        assertTrue(topUpFound, "Transaksi TOP_UP tidak ditemukan");
        assertTrue(withdrawalFound, "Transaksi WITHDRAWAL tidak ditemukan");
        assertTrue(donationFound, "Transaksi DONATION tidak ditemukan");
    }
}