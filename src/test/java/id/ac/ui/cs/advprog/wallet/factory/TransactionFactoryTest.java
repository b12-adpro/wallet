package id.ac.ui.cs.advprog.wallet.factory;

import id.ac.ui.cs.advprog.wallet.model.transaction.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class TransactionFactoryTest {
    @Test
    public void testCreateTopUpTransaction() {
        Transaction t = TransactionFactory.createTransaction("TOP_UP", new BigDecimal("1000"));
        assertNotNull(t);
        assertEquals("TOP_UP", t.getType());
        assertEquals(new BigDecimal("1000"), t.getAmount());
    }

    @Test
    public void testCreateWithdrawalTransaction() {
        Transaction t = TransactionFactory.createTransaction("WITHDRAWAL", new BigDecimal("500"));
        assertNotNull(t);
        assertEquals("WITHDRAWAL", t.getType());
        assertEquals(new BigDecimal("500"), t.getAmount());
    }

    @Test
    public void testCreateDonationTransaction() {
        Transaction t = TransactionFactory.createTransaction("DONATION", new BigDecimal("200"));
        assertNotNull(t);
        assertEquals("DONATION", t.getType());
        assertEquals(new BigDecimal("200"), t.getAmount());
    }
}