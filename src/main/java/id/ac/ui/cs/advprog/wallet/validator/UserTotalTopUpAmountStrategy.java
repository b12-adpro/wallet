package id.ac.ui.cs.advprog.wallet.validator;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository; 

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class UserTotalTopUpAmountStrategy implements ValidationStrategy {
    private final TransactionRepository transactionRepository;
    private final BigDecimal dailyTopUpLimit; 

    public UserTotalTopUpAmountStrategy(TransactionRepository transactionRepository, BigDecimal dailyTopUpLimit) {
        this.transactionRepository = transactionRepository;
        this.dailyTopUpLimit = dailyTopUpLimit;
    }

    @Override
    public boolean validate(ValidationContext context) {
        if (context.getUserId() == null || context.getAmountToValidate() == null) {
            return false; 
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<TransactionEntity> todaysTopUps = transactionRepository.findByWalletUserId(context.getUserId())
                .stream()
                .filter(tx -> "TOP_UP".equalsIgnoreCase(tx.getType()) &&
                               !tx.getTimestamp().isBefore(startOfDay) &&
                               !tx.getTimestamp().isAfter(endOfDay))
                .toList();

        BigDecimal totalTodaysTopUpAmount = todaysTopUps.stream()
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal projectedTotal = totalTodaysTopUpAmount.add(context.getAmountToValidate());

        return projectedTotal.compareTo(dailyTopUpLimit) <= 0;
    }

    @Override
    public String getErrorMessage() {
        return "This top-up will exceed your daily top-up limit of " + dailyTopUpLimit + ".";
    }
}