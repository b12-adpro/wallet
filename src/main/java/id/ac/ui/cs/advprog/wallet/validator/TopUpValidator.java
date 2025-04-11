package id.ac.ui.cs.advprog.wallet.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TopUpValidator {

    private List<ValidationStrategy> strategies = new ArrayList<>();

    public TopUpValidator() {
        strategies.add(new NegativeValidationStrategy());
    }

    public boolean validate(BigDecimal amount) {
        return strategies.stream().allMatch(strategy -> strategy.validate(amount));
    }
}