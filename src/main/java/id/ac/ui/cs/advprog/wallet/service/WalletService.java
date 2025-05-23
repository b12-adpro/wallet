package id.ac.ui.cs.advprog.wallet.service;

import id.ac.ui.cs.advprog.wallet.model.Wallet;

import java.util.UUID;

public interface WalletService {
    Wallet getWallet(UUID userId);
    void topUpWallet(UUID userId, String amountStr);
    void withdrawCampaign(UUID userId, String amountStr, UUID campaignId);
    void donateWallet(UUID userId, String amountStr, UUID campaignId, UUID donationId);
}