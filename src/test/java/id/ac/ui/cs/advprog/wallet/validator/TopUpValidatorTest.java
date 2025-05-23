package id.ac.ui.cs.advprog.wallet.validator;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
public class TopUpValidatorTest {

    @Mock 
    private TransactionRepository transactionRepository;

    private TopUpValidator validator;

    private final BigDecimal minAmount = TopUpValidator.DEFAULT_MIN_TOP_UP_AMOUNT;
    private final BigDecimal maxAmount = TopUpValidator.DEFAULT_MAX_TOP_UP_AMOUNT;
    private final BigDecimal walletMaxBalance = TopUpValidator.DEFAULT_WALLET_ABSOLUTE_MAX_BALANCE;
    private final BigDecimal dailyLimit = TopUpValidator.DEFAULT_USER_DAILY_TOP_UP_LIMIT;

    private UUID testUserId;
    private BigDecimal currentBalanceLow; 
    private BigDecimal currentBalanceHigh; 

    @BeforeEach
    void setUp() {
        validator = new TopUpValidator(transactionRepository);
        testUserId = UUID.randomUUID();
        currentBalanceLow = new BigDecimal("1000000"); 
        currentBalanceHigh = walletMaxBalance.subtract(new BigDecimal("5000")); 
    }

    @Test
    void testAmountBelowMinimum_shouldFail() {
        BigDecimal amountToValidate = minAmount.subtract(BigDecimal.ONE); 
        assertFalse(validator.validate(testUserId, amountToValidate, currentBalanceLow));
        assertTrue(validator.getErrorMessages().contains("Amount must be at least " + minAmount + "."));
    }

    @Test
    void testAmountEqualToMinimum_shouldPassMinimumValidation() {
        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(new ArrayList<>());
        assertTrue(validator.validate(testUserId, minAmount, currentBalanceLow));
    }

    @Test
    void testAmountAboveMaximum_shouldFail() {
        BigDecimal amountToValidate = maxAmount.add(BigDecimal.ONE); 
        assertFalse(validator.validate(testUserId, amountToValidate, currentBalanceLow));
        assertTrue(validator.getErrorMessages().contains("Amount must not exceed " + maxAmount + "."));
    }

    @Test
    void testAmountEqualToMaximum_shouldPassMaximumValidation() {
        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(new ArrayList<>());
        BigDecimal veryLowBalance = BigDecimal.ZERO;
        assertTrue(validator.validate(testUserId, maxAmount, veryLowBalance));
    }

    @Test
    void testTopUpExceedsWalletMaxBalance_shouldFail() {
        BigDecimal amountToValidate = new BigDecimal("10000"); 
        assertFalse(validator.validate(testUserId, amountToValidate, currentBalanceHigh));
        assertTrue(validator.getErrorMessages().contains("This top-up will exceed the maximum allowed wallet balance of " + walletMaxBalance + "."));
    }

    @Test
    void testTopUpDoesNotExceedWalletMaxBalance_shouldPassWalletMaxBalanceValidation() {
        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(new ArrayList<>());
        BigDecimal amountToValidate = new BigDecimal("10000");
        assertTrue(validator.validate(testUserId, amountToValidate, currentBalanceLow));
    }

    @Test
    void testTopUpExceedsDailyLimit_shouldFail() {
        List<TransactionEntity> previousTopUps = new ArrayList<>();
        BigDecimal amountAlreadyToppedUp = dailyLimit.subtract(new BigDecimal("5000"));
        previousTopUps.add(new TransactionEntity("TOP_UP", amountAlreadyToppedUp, LocalDateTime.now(), null));

        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(previousTopUps);

        BigDecimal amountToValidate = new BigDecimal("10000"); 
        assertFalse(validator.validate(testUserId, amountToValidate, currentBalanceLow));
        assertTrue(validator.getErrorMessages().contains("This top-up will exceed your daily top-up limit of " + dailyLimit + "."));
    }

    @Test
    void testTopUpDoesNotExceedDailyLimit_shouldPassDailyLimitValidation() {
        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(new ArrayList<>());
        BigDecimal amountToValidate = new BigDecimal("10000"); 
        assertTrue(validator.validate(testUserId, amountToValidate, currentBalanceLow));
    }

    @Test
    void testTopUpWithPreviousTopUpsButWithinDailyLimit_shouldPassDailyLimitValidation() {
        List<TransactionEntity> previousTopUps = new ArrayList<>();
        previousTopUps.add(new TransactionEntity("TOP_UP", new BigDecimal("1000000"), LocalDateTime.now(), null));
        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(previousTopUps);

        BigDecimal amountToValidate = new BigDecimal("1000000"); 
        assertTrue(validator.validate(testUserId, amountToValidate, currentBalanceLow));
    }

    @Test
    void testValidTopUpAmount_AllConditionsMet_shouldPass() {
        when(transactionRepository.findByWalletUserId(testUserId)).thenReturn(new ArrayList<>());
        BigDecimal amountToValidate = new BigDecimal("50000"); 
        assertTrue(validator.validate(testUserId, amountToValidate, currentBalanceLow));
        assertTrue(validator.getErrorMessages().isEmpty());
    }

    @Test
    void testMultipleErrors_shouldContainAllRelevantErrorMessages() {
        BigDecimal amountBelowMin = minAmount.subtract(BigDecimal.ONE); 
        assertFalse(validator.validate(testUserId, amountBelowMin, currentBalanceHigh));

        List<String> errors = validator.getErrorMessages();
        assertEquals(2, errors.size()); // Harusnya ada 2 error
        assertTrue(errors.contains("Amount must be at least " + minAmount + "."));
        assertTrue(errors.contains("This top-up will exceed the maximum allowed wallet balance of " + walletMaxBalance + "."));
    }

    @Test
    void testConstructorWithCustomLimits() {
        BigDecimal customMin = new BigDecimal("5000");
        BigDecimal customMax = new BigDecimal("500000");
        BigDecimal customWalletMax = new BigDecimal("1000000");
        BigDecimal customDailyLimit = new BigDecimal("200000");

        TopUpValidator customValidator = new TopUpValidator(
                transactionRepository, customMin, customMax, customWalletMax, customDailyLimit
        );

        assertFalse(customValidator.validate(testUserId, new BigDecimal("4000"), currentBalanceLow));
        assertTrue(customValidator.getErrorMessages().contains("Amount must be at least " + customMin + "."));
    }

    @Test
    void testConstructorThrowsExceptionIfMinGreaterThanMax() {
        BigDecimal invalidMin = new BigDecimal("100000");
        BigDecimal invalidMax = new BigDecimal("10000"); 

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TopUpValidator(transactionRepository, invalidMin, invalidMax, walletMaxBalance, dailyLimit);
        });
        assertEquals("Min top-up amount cannot be greater than max top-up amount.", exception.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionIfMinIsNegative() {
        BigDecimal negativeMin = new BigDecimal("-100");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TopUpValidator(transactionRepository, negativeMin, maxAmount, walletMaxBalance, dailyLimit);
        });
        assertEquals("Min top-up amount cannot be negative.", exception.getMessage());
    }
}