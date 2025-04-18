package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;
    
    @Test
    public void testSaveAndFindById() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal("0"));
        wallet = walletRepository.save(wallet);
        
        assertThat(wallet.getId()).isNotNull();
        Wallet retrieved = walletRepository.findById(wallet.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getBalance()).isEqualTo(BigDecimal.ZERO);
        
        assertThat(walletRepository.findByUserId(1L).orElse(null)).isNotNull();
    }
}