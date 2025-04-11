package id.ac.ui.cs.advprog.wallet.observer;

import id.ac.ui.cs.advprog.wallet.model.Wallet;

public class NotificationService implements WalletObserver {
    @Override
    public void update(Wallet wallet) {
        System.out.println("Notifikasi: Wallet untuk userId " + wallet.getUserId() +
                           " telah diupdate. Saldo baru: " + wallet.getBalance());
    }
}