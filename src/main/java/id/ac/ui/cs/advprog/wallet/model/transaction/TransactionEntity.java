package id.ac.ui.cs.advprog.wallet.model.transaction;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
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

    public TransactionEntity() {}

    public TransactionEntity(String type, BigDecimal amount, LocalDateTime timestamp, Wallet wallet) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.wallet = wallet;
    }

    public UUID getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public Wallet getWallet() {
        return wallet;
    }
}