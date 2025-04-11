package id.ac.ui.cs.advprog.wallet.observer;

import id.ac.ui.cs.advprog.wallet.model.Wallet;

public interface WalletObserver {
    void update(Wallet wallet);
}