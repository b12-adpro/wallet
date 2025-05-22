package id.ac.ui.cs.advprog.wallet.controller;

import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;
import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

@RestController
@RequestMapping("/api/wallet")
@PreAuthorize("hasRole('USER')")
public class WalletController {

    private final WalletService walletService;
    
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }
    
    private UUID extractUserId(Authentication auth) {
        Object principal = auth.getPrincipal();
        if (principal instanceof String) {
            return UUID.fromString((String) principal);
        } else if (principal instanceof User) {
            return UUID.fromString(((User) principal).getUsername());
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
    }


    @GetMapping("")
    public ResponseEntity<GeneralResponse> getWallet(Authentication auth) {
        UUID userId = extractUserId(auth);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/topup")
    public ResponseEntity<GeneralResponse> topUpWallet(Authentication auth,
                                                       @RequestParam("amount") String amount) {          
        UUID userId = extractUserId(auth);
        walletService.topUpWallet(userId, amount);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Top-up successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/donate")
    public ResponseEntity<GeneralResponse> donate(Authentication auth,
                                                  @RequestParam("amount") String amount) {
        UUID userId = extractUserId(auth);
        walletService.donateWallet(userId, amount);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Donation successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/withdrawals")
    public ResponseEntity<GeneralResponse> withdrawals(Authentication auth,
                                                       @RequestParam("amount") String amount) {
        UUID userId = extractUserId(auth);
        walletService.withdrawCampaign(userId, amount);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Campaign withdrawal successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}