package id.ac.ui.cs.advprog.wallet.observer;

import id.ac.ui.cs.advprog.wallet.model.Wallet;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements WalletObserver {
    @Override
    public void update(Wallet wallet) {
        System.out.println("Notifikasi: Wallet untuk userId " + wallet.getUserId() +
                           " telah diupdate. Saldo baru: " + wallet.getBalance());
    }
}