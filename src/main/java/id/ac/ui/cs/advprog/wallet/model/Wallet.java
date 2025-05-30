package id.ac.ui.cs.advprog.wallet.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*; 
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
public class Wallet {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM) 
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id; 

    @Column(name = "user_id", unique = true, columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "balance")
    private BigDecimal balance;

    public Wallet() {
        this.balance = BigDecimal.ZERO;
    }
}