package id.ac.ui.cs.advprog.wallet.validator;

import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository; 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TopUpValidator {

    private final List<ValidationStrategy> strategies = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();

    public static final BigDecimal DEFAULT_MIN_TOP_UP_AMOUNT = new BigDecimal("10000");
    public static final BigDecimal DEFAULT_MAX_TOP_UP_AMOUNT = new BigDecimal("5000000");
    public static final BigDecimal DEFAULT_WALLET_ABSOLUTE_MAX_BALANCE = new BigDecimal("100000000"); 
    public static final BigDecimal DEFAULT_USER_DAILY_TOP_UP_LIMIT = new BigDecimal("5000000"); 

    public TopUpValidator(TransactionRepository transactionRepository) {
        initializeStrategies(
                transactionRepository, 
                DEFAULT_MIN_TOP_UP_AMOUNT,
                DEFAULT_MAX_TOP_UP_AMOUNT,
                DEFAULT_WALLET_ABSOLUTE_MAX_BALANCE,
                DEFAULT_USER_DAILY_TOP_UP_LIMIT
        );
    }

    public TopUpValidator(TransactionRepository transactionRepository,
                          BigDecimal minAmount, BigDecimal maxAmount,
                          BigDecimal walletAbsoluteMaxBalance, BigDecimal userDailyTopUpLimit) {
        if (minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Min top-up amount cannot be greater than max top-up amount.");
        }
        if (minAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Min top-up amount cannot be negative.");
        }
        initializeStrategies(transactionRepository, minAmount, maxAmount, walletAbsoluteMaxBalance, userDailyTopUpLimit);
    }

    private void initializeStrategies(TransactionRepository transactionRepository,
                                      BigDecimal minAmount, BigDecimal maxAmount,
                                      BigDecimal walletAbsoluteMaxBalance, BigDecimal userDailyTopUpLimit) {
        strategies.add(new MinimumAmountValidationStrategy(minAmount)); 
        strategies.add(new MaximumAmountValidationStrategy(maxAmount)); 
        strategies.add(new WalletMaxBalanceValidationStrategy(walletAbsoluteMaxBalance));
        if (transactionRepository != null) { 
             strategies.add(new UserTotalTopUpAmountStrategy(transactionRepository, userDailyTopUpLimit));
        }
    }

    public boolean validate(UUID userId, BigDecimal amountToValidate, BigDecimal currentWalletBalance) {
        errorMessages.clear();

        ValidationContext context = ValidationContext.builder()
                .userId(userId)
                .amountToValidate(amountToValidate)
                .currentWalletBalance(currentWalletBalance)
                .build();

        boolean isValid = true;
        for (ValidationStrategy strategy : strategies) {
            if (!strategy.validate(context)) {
                errorMessages.add(strategy.getErrorMessage());
                isValid = false;
            }
        }
        return isValid;
    }

    public List<String> getErrorMessages() {
        return new ArrayList<>(errorMessages);
    }
}