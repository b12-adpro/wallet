package id.ac.ui.cs.advprog.wallet.model;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {
    @Test
    public void testDefaultBalance() {
        // Ekspektasi: Instans baru Wallet harus punya saldo 0.
        Wallet wallet = new Wallet();
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
    }
}