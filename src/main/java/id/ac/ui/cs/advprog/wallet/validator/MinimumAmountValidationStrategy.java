package id.ac.ui.cs.advprog.wallet.validator;

import java.math.BigDecimal;

public class MinimumAmountValidationStrategy implements ValidationStrategy {
    private final BigDecimal minAmount;

    public MinimumAmountValidationStrategy(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    @Override
    public boolean validate(ValidationContext context) {
        if (context.getAmountToValidate() == null) return false;
        return context.getAmountToValidate().compareTo(minAmount) >= 0;
    }

    @Override
    public String getErrorMessage() {
        return "Amount must be at least " + minAmount + ".";
    }
}