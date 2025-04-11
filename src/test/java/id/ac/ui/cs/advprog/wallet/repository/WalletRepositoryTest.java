package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class WalletRepositoryTest {
    @Test
    public void testSaveAndFindById() {

        WalletRepository repository = new InMemoryWalletRepository();
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(new BigDecimal("0"));
        repository.save(wallet);
        
        Wallet retrieved = repository.findById(1L).orElse(null);
        assertNotNull(retrieved);
        assertEquals(BigDecimal.ZERO, retrieved.getBalance());
    }
}