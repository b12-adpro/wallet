package id.ac.ui.cs.advprog.wallet.factory;

import id.ac.ui.cs.advprog.wallet.model.transaction.DonationTransaction;
import id.ac.ui.cs.advprog.wallet.model.transaction.Transaction;
import id.ac.ui.cs.advprog.wallet.model.transaction.TopUpTransaction;
import id.ac.ui.cs.advprog.wallet.model.transaction.WithdrawalTransaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.UUID;

public class TransactionFactoryTest {
    @Test
    void testCreateTopUpTransaction() {
        Transaction t = TransactionFactory.createTransaction("TOP_UP", new BigDecimal("1000"));
        assertNotNull(t);
        assertTrue(t instanceof TopUpTransaction);
        assertEquals("TOP_UP", t.getType());
        assertEquals(0, new BigDecimal("1000").compareTo(t.getAmount()));
    }

    @Test
    void testCreateWithdrawalTransaction_withFactoryMethod() {
        UUID campaignId = UUID.randomUUID();
        Transaction t = TransactionFactory.createWithdrawalTransaction(new BigDecimal("500"), campaignId);
        assertNotNull(t);
        assertTrue(t instanceof WithdrawalTransaction);
        assertEquals(campaignId, ((WithdrawalTransaction) t).getCampaignId());
    }

    @Test
    void testCreateDonationTransaction_withFactoryMethod_withDonationId() {
        UUID campaignId = UUID.randomUUID();
        UUID donationId = UUID.randomUUID(); 
        Transaction t = TransactionFactory.createDonationTransaction(new BigDecimal("200"), campaignId, donationId);
        assertNotNull(t);
        assertTrue(t instanceof DonationTransaction);
        assertEquals("DONATION", t.getType());
        assertEquals(0, new BigDecimal("200").compareTo(t.getAmount()));
        assertEquals(campaignId, ((DonationTransaction) t).getCampaignId());
        assertEquals(donationId, ((DonationTransaction) t).getDonationId()); 
    }

    @Test
    void testCreateTransaction_invalidType_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionFactory.createTransaction("INVALID_TYPE", new BigDecimal("100"));
        });
    }

    @Test
    void testCreateTransaction_forWithdrawalOrDonation_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionFactory.createTransaction("WITHDRAWAL", new BigDecimal("100"));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionFactory.createTransaction("DONATION", new BigDecimal("100"));
        });
    }
}