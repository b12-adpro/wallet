package id.ac.ui.cs.advprog.wallet.model.transaction;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class WithdrawalTransaction implements Transaction {
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public WithdrawalTransaction(BigDecimal amount) {
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String getType() {
        return "WITHDRAWAL";
    }
}