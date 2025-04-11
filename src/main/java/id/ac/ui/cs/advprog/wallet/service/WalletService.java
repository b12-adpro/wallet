package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;

public interface WalletService {
    Wallet getWallet(Long userId);
    void topUpWallet(Long userId, String amountStr);
}