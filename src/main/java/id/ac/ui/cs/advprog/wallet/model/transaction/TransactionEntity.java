package id.ac.ui.cs.advprog.wallet.model.transaction;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import jakarta.persistence.*;
import lombok.Getter; 
import lombok.Setter; 

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter 
@Setter 
public class TransactionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "campaign_id", nullable = true) 
    private UUID campaignId; 

    @Column(name = "donation_id", nullable = true) 
    private UUID donationId;   

    public TransactionEntity() {}

    public TransactionEntity(String type, BigDecimal amount, LocalDateTime timestamp, Wallet wallet,
                             UUID campaignId, UUID donationId) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.wallet = wallet;
        this.campaignId = campaignId;
        this.donationId = donationId;
    }

    public TransactionEntity(String type, BigDecimal amount, LocalDateTime timestamp, Wallet wallet) {
        this(type, amount, timestamp, wallet, null, null);
    }
}