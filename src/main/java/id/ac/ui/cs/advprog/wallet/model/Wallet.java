package id.ac.ui.cs.advprog.wallet.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@Getter
@Setter
public class Wallet {
    @Id
    private Long id;
    
    private BigDecimal balance;
    
    public Wallet() {
        this.balance = BigDecimal.ZERO;
    }
}