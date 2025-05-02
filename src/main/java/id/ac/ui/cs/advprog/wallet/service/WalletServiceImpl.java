package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import id.ac.ui.cs.advprog.wallet.factory.TransactionFactory;
import id.ac.ui.cs.advprog.wallet.model.transaction.Transaction; 
import id.ac.ui.cs.advprog.wallet.repository.TransactionRepository;
import id.ac.ui.cs.advprog.wallet.repository.WalletRepository;
import id.ac.ui.cs.advprog.wallet.observer.WalletObserver;
import id.ac.ui.cs.advprog.wallet.observer.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository; 
    private List<WalletObserver> observers = new ArrayList<>();

    public WalletServiceImpl(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId).orElseGet(() -> {
            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            return walletRepository.save(wallet);
        });
    }

    @Override
    public void topUpWallet(Long userId, String amountStr) {
        int topUpAmount = Integer.parseInt(amountStr);
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance().add(new BigDecimal(topUpAmount)));
        walletRepository.save(wallet);

        // Buat dan simpan transaksi TOP_UP
        Transaction topUp = TransactionFactory.createTransaction("TOP_UP", new BigDecimal(topUpAmount));
        TransactionEntity trxEntity = new TransactionEntity(
                topUp.getType(), topUp.getAmount(), topUp.getTimestamp(), wallet);
        transactionRepository.save(trxEntity);

        if (observers.isEmpty()) { observers.add(new NotificationService()); }
        observers.forEach(o -> o.update(wallet));
    }

    @Override
    public void withdrawWallet(Long userId, String amountStr) {
        int withdrawAmount = Integer.parseInt(amountStr);
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance().subtract(new BigDecimal(withdrawAmount)));
        walletRepository.save(wallet);

        // Buat dan simpan transaksi WITHDRAWAL
        Transaction withdrawal = TransactionFactory.createTransaction("WITHDRAWAL", new BigDecimal(withdrawAmount));
        TransactionEntity trxEntity = new TransactionEntity(
                withdrawal.getType(), withdrawal.getAmount(), withdrawal.getTimestamp(), wallet);
        transactionRepository.save(trxEntity);

        if (observers.isEmpty()) { observers.add(new NotificationService()); }
        observers.forEach(o -> o.update(wallet));
    }

    @Override
    public void donateWallet(Long userId, String amountStr) {
        int donationAmount = Integer.parseInt(amountStr);
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance().subtract(new BigDecimal(donationAmount)));
        walletRepository.save(wallet);

        // Buat dan simpan transaksi DONATION
        Transaction donation = TransactionFactory.createTransaction("DONATION", new BigDecimal(donationAmount));
        TransactionEntity trxEntity = new TransactionEntity(
                donation.getType(), donation.getAmount(), donation.getTimestamp(), wallet);
        transactionRepository.save(trxEntity);

        if (observers.isEmpty()) { observers.add(new NotificationService()); }
        observers.forEach(o -> o.update(wallet));
    }

    @Override
    public void receiveDonation(Long userId, String amountStr) {
        BigDecimal donationAmount = new BigDecimal(amountStr);
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance().add(donationAmount));
        walletRepository.save(wallet);

        if (observers.isEmpty()) {
            observers.add(new NotificationService());
        }
        observers.forEach(o -> o.update(wallet));
    }
}