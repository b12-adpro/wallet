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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @Test
    public void testGetWallet() {
        Long userId = 1L;
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        when(walletService.getWallet(userId)).thenReturn(wallet);

        ResponseEntity<GeneralResponse> response = walletController.getWallet(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("OK", body.getStatus());
        assertEquals("Success", body.getMessage());
        Wallet returnedWallet = (Wallet) body.getData();
        assertEquals(userId, returnedWallet.getUserId());
    }

    @Test
    public void testTopUpWallet() {
        Long userId = 1L;
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(new java.math.BigDecimal("1000"));
        when(walletService.getWallet(userId)).thenReturn(wallet);

        ResponseEntity<GeneralResponse> response = walletController.topUpWallet(userId, "1000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("OK", body.getStatus());
        assertEquals("Top-up successful", body.getMessage());
        Wallet returnedWallet = (Wallet) body.getData();
        assertEquals(new java.math.BigDecimal("1000"), returnedWallet.getBalance());
    }

    @Test
    public void testDonate() {
        Long userId = 1L;
        // Belum diimplementasikan, sehingga stub response diharapkan.
        ResponseEntity<GeneralResponse> response = walletController.donate(userId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("Not implemented", body.getMessage());
    }

    @Test
    public void testWithdrawals() {
        Long userId = 1L;
        // Belum diimplementasikan, sehingga stub response diharapkan.
        ResponseEntity<GeneralResponse> response = walletController.withdrawals(userId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
        GeneralResponse body = response.getBody();
        assertEquals("Not implemented", body.getMessage());
    }
}