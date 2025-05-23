package id.ac.ui.cs.advprog.wallet.model.transaction;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID; 

@Getter
@Setter
public class WithdrawalTransaction implements Transaction {
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private UUID campaignId; 

    public WithdrawalTransaction(BigDecimal amount, UUID campaignId) { 
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.campaignId = campaignId; 
    }

    @Override
    public String getType() {
        return "WITHDRAWAL";
    }
}