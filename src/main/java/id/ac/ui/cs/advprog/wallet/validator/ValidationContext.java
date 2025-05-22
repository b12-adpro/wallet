package id.ac.ui.cs.advprog.wallet.validator;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ValidationContext {
    private final BigDecimal amountToValidate; 
    private final UUID userId;                 
    private final BigDecimal currentWalletBalance; 
}