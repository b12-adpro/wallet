package id.ac.ui.cs.advprog.wallet.validator;

import java.math.BigDecimal;

public class MaximumAmountValidationStrategy implements ValidationStrategy {
    private final BigDecimal maxAmount;

    public MaximumAmountValidationStrategy(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean validate(ValidationContext context) {
        if (context.getAmountToValidate() == null) return false;
        return context.getAmountToValidate().compareTo(maxAmount) <= 0;
    }

    @Override
    public String getErrorMessage() {
        return "Amount must not exceed " + maxAmount + ".";
    }
}