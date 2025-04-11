package id.ac.ui.cs.advprog.wallet.model.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Transaction {
    String getType();
    BigDecimal getAmount();
    LocalDateTime getTimestamp();
}