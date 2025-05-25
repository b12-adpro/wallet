package id.ac.ui.cs.advprog.wallet.controller;

import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;
import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull; 
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @Test
    void testGetWallet() {
        UUID userId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO); 
        when(walletService.getWallet(userId)).thenReturn(wallet);

        ResponseEntity<GeneralResponse> response = walletController.getWallet(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Success", body.getMessage());
        Wallet returnedWallet = (Wallet) body.getData();
        assertEquals(userId, returnedWallet.getUserId());
        assertEquals(0, BigDecimal.ZERO.compareTo(returnedWallet.getBalance()));
    }

    @Test
    void testTopUpWallet() {
        UUID userId = UUID.randomUUID();
        String amount = "10000";
        Wallet walletAfterTopUp = new Wallet();
        walletAfterTopUp.setUserId(userId);
        walletAfterTopUp.setBalance(new BigDecimal(amount));

        when(walletService.getWallet(userId)).thenReturn(walletAfterTopUp);

        ResponseEntity<GeneralResponse> response = walletController.topUpWallet(userId, amount);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Top-up successful", body.getMessage());
        Wallet returnedWallet = (Wallet) body.getData();
        assertEquals(0, new BigDecimal(amount).compareTo(returnedWallet.getBalance()));
        verify(walletService, times(1)).topUpWallet(userId, amount);
    }

    @Test
    void testDonate_withCampaignAndDonationIdParams() {
        UUID userId = UUID.randomUUID();
        UUID campaignId = UUID.randomUUID();
        UUID donationId = UUID.randomUUID(); 
        String amount = "3000";

        Wallet walletAfterDonation = new Wallet();
        walletAfterDonation.setUserId(userId);
        walletAfterDonation.setBalance(new BigDecimal("7000")); 

        doNothing().when(walletService).donateWallet(userId, amount, campaignId, donationId);
        when(walletService.getWallet(userId)).thenReturn(walletAfterDonation);


        ResponseEntity<GeneralResponse> response = walletController.donate(userId, amount, campaignId, donationId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Donation successful", body.getMessage());
        Wallet returnedWallet = (Wallet) body.getData();
        assertEquals(0, new BigDecimal("7000").compareTo(returnedWallet.getBalance()));
        verify(walletService, times(1)).donateWallet(userId, amount, campaignId, donationId);
    }

    @Test
    void testWithdrawals() {
        UUID userId = UUID.randomUUID();
        UUID campaignId = UUID.randomUUID();
        String amount = "5000";

        Wallet walletAfterWithdrawal = new Wallet();
        walletAfterWithdrawal.setUserId(userId);
        walletAfterWithdrawal.setBalance(new BigDecimal("5000"));

        doNothing().when(walletService).withdrawCampaign(userId, amount, campaignId);
        when(walletService.getWallet(userId)).thenReturn(walletAfterWithdrawal);


        ResponseEntity<GeneralResponse> response = walletController.withdrawals(userId, amount, campaignId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getStatus());
        assertEquals("Campaign withdrawal successful", body.getMessage());
        Wallet returnedWallet = (Wallet) body.getData();
        assertEquals(0, new BigDecimal("5000").compareTo(returnedWallet.getBalance()));
        verify(walletService, times(1)).withdrawCampaign(userId, amount, campaignId);
    }
}