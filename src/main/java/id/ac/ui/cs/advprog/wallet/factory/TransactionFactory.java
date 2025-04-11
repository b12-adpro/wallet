package id.ac.ui.cs.advprog.wallet.factory;

import id.ac.ui.cs.advprog.wallet.model.transaction.*;
import java.math.BigDecimal;

public class TransactionFactory {
    public static Transaction createTransaction(String type, BigDecimal amount) {
        switch (type) {
            case "TOP_UP":
                return new TopUpTransaction(amount);
            case "WITHDRAWAL":
                return new WithdrawalTransaction(amount);
            case "DONATION":
                return new DonationTransaction(amount);
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }
}