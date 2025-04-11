package id.ac.ui.cs.advprog.wallet.validator;

import java.math.BigDecimal;

public class NegativeValidationStrategy implements ValidationStrategy {
    @Override
    public boolean validate(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}