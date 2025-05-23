package id.ac.ui.cs.advprog.wallet.validator;

public interface ValidationStrategy {
    boolean validate(ValidationContext context); 
    String getErrorMessage();
}