package id.ac.ui.cs.advprog.wallet.model.transaction;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID; 

@Getter
@Setter
public class DonationTransaction implements Transaction {
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private UUID campaignId; 
    private UUID donationId;  

    public DonationTransaction(BigDecimal amount, UUID campaignId, UUID donationId) { 
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.campaignId = campaignId; 
        this.donationId = donationId;   
    }

    @Override
    public String getType() {
        return "DONATION";
    }
}