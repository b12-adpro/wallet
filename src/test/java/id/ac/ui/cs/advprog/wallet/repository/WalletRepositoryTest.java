package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;
    
    @Test
    public void testSaveAndFindById() {
        Wallet wallet = new Wallet();
        UUID uuid_generated = UUID.randomUUID();
        wallet.setUserId(uuid_generated);
        wallet.setBalance(new BigDecimal("0"));
        wallet = walletRepository.save(wallet);
        
        assertThat(wallet.getId()).isNotNull();
        Wallet retrieved = walletRepository.findById(wallet.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getBalance()).isEqualTo(BigDecimal.ZERO);
        
        assertThat(walletRepository.findByUserId(uuid_generated).orElse(null)).isNotNull();
    }
}