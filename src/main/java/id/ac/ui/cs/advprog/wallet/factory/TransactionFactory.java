package id.ac.ui.cs.advprog.wallet.factory;

import id.ac.ui.cs.advprog.wallet.model.transaction.*;
import java.math.BigDecimal;
import java.util.UUID; 

public class TransactionFactory {

    public static Transaction createTransaction(String type, BigDecimal amount) {
        if ("TOP_UP".equals(type)) {
            return new TopUpTransaction(amount);
        }
        throw new IllegalArgumentException("Unknown transaction type or missing required IDs for type: " + type);
    }

    public static Transaction createWithdrawalTransaction(BigDecimal amount, UUID campaignId) {
        return new WithdrawalTransaction(amount, campaignId);
    }

    public static Transaction createDonationTransaction(BigDecimal amount, UUID campaignId, UUID donationId) {
        return new DonationTransaction(amount, campaignId, donationId);
    }
}