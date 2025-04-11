package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
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
    private List<WalletObserver> observers = new ArrayList<>();

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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

        if (observers.isEmpty()) {
            observers.add(new NotificationService());
        }
        observers.forEach(o -> o.update(wallet));
    }
}