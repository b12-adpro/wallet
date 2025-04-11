package id.ac.ui.cs.advprog.wallet.validator;

import java.math.BigDecimal;

public interface ValidationStrategy {
    boolean validate(BigDecimal amount);
}