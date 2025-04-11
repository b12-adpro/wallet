package id.ac.ui.cs.advprog.wallet.validator;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class TopUpValidatorTest {
    @Test
    public void testNegativeAmountValidation() {
        TopUpValidator validator = new TopUpValidator();
        assertFalse(validator.validate(new BigDecimal("-500")));
    }
    
    @Test
    public void testPositiveAmountValidation() {
        TopUpValidator validator = new TopUpValidator();
        assertTrue(validator.validate(new BigDecimal("1000")));
    }
}