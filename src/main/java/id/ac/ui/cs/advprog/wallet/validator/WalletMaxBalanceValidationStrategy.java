package id.ac.ui.cs.advprog.wallet.validator;

import java.math.BigDecimal;

public class WalletMaxBalanceValidationStrategy implements ValidationStrategy {
    private final BigDecimal walletAbsoluteMaxBalance; 

    public WalletMaxBalanceValidationStrategy(BigDecimal walletAbsoluteMaxBalance) {
        this.walletAbsoluteMaxBalance = walletAbsoluteMaxBalance;
    }

    @Override
    public boolean validate(ValidationContext context) {
        if (context.getCurrentWalletBalance() == null || context.getAmountToValidate() == null) {
            return false;
        }
        BigDecimal futureBalance = context.getCurrentWalletBalance().add(context.getAmountToValidate());
        return futureBalance.compareTo(walletAbsoluteMaxBalance) <= 0;
    }

    @Override
    public String getErrorMessage() {
        return "This top-up will exceed the maximum allowed wallet balance of " + walletAbsoluteMaxBalance + ".";
    }
}