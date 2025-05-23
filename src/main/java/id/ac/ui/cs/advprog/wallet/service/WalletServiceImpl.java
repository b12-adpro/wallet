package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.factory.TransactionFactory;
import id.ac.ui.cs.advprog.wallet.model.transaction.Transaction; 
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import id.ac.ui.cs.advprog.wallet.repository.WalletRepository;
import id.ac.ui.cs.advprog.wallet.validator.TopUpValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository; 
    private final TopUpValidator topUpValidator;

    public WalletServiceImpl(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.topUpValidator = new TopUpValidator(transactionRepository);
    }

    @Override
    public Wallet getWallet(UUID userId) {
        return walletRepository.findByUserId(userId).orElseGet(() -> {
            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            return walletRepository.save(wallet);
        });
    }

    @Override
    public void topUpWallet(UUID userId, String amountStr) {
        BigDecimal amountDecimal;
        try {
            amountDecimal = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + amountStr + ". Must be a valid number.");
        }

        Wallet currentWallet = getWallet(userId);

        if (!topUpValidator.validate(userId, amountDecimal, currentWallet.getBalance())) {
            String combinedErrorMessage = String.join("; ", topUpValidator.getErrorMessages());
            throw new IllegalArgumentException(combinedErrorMessage);
        }

        currentWallet.setBalance(currentWallet.getBalance().add(amountDecimal));
        walletRepository.save(currentWallet);

        Transaction topUp = TransactionFactory.createTransaction("TOP_UP", amountDecimal);
        TransactionEntity trxEntity = new TransactionEntity(
                topUp.getType(), topUp.getAmount(), topUp.getTimestamp(), currentWallet,
                null, null); 
        transactionRepository.save(trxEntity);
    }

    @Override
    public void withdrawCampaign(UUID userId, String amountStr, UUID campaignId) { 
        BigDecimal withdrawAmountDecimal;
        try {
            withdrawAmountDecimal = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid withdrawal amount format: " + amountStr + ". Must be a valid number.");
        }

        if (withdrawAmountDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (campaignId == null) { 
            throw new IllegalArgumentException("CampaignId is required for withdrawal.");
        }

        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance().add(withdrawAmountDecimal)); 
        walletRepository.save(wallet);

        Transaction withdrawal = TransactionFactory.createWithdrawalTransaction(withdrawAmountDecimal, campaignId);
        TransactionEntity trxEntity = new TransactionEntity(
                withdrawal.getType(), withdrawal.getAmount(), withdrawal.getTimestamp(), wallet,
                campaignId, null); 
        transactionRepository.save(trxEntity);
    }

    @Override
    public void donateWallet(UUID userId, String amountStr, UUID campaignId, UUID donationId) { 
        BigDecimal donationAmountDecimal;
        try {
            donationAmountDecimal = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid donation amount format: " + amountStr + ". Must be a valid number.");
        }

        if (donationAmountDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Donation amount must be positive.");
        }
        if (campaignId == null || donationId == null) { 
            throw new IllegalArgumentException("CampaignId and DonationId are required for donation.");
        }

        Wallet wallet = getWallet(userId);
        if (wallet.getBalance().compareTo(donationAmountDecimal) < 0) {
            throw new IllegalArgumentException("Insufficient balance for donation. Current balance: " + wallet.getBalance() + ", Donation amount: " + donationAmountDecimal);
        }

        wallet.setBalance(wallet.getBalance().subtract(donationAmountDecimal));
        walletRepository.save(wallet);

        Transaction donation = TransactionFactory.createDonationTransaction(donationAmountDecimal, campaignId, donationId);
        TransactionEntity trxEntity = new TransactionEntity(
                donation.getType(), donation.getAmount(), donation.getTimestamp(), wallet,
                campaignId, donationId); 
        transactionRepository.save(trxEntity);
    }
}