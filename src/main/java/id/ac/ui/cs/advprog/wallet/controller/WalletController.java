package id.ac.ui.cs.advprog.wallet.controller;

import id.ac.ui.cs.advprog.wallet.dto.GeneralResponse;
import id.ac.ui.cs.advprog.wallet.model.Wallet;
import id.ac.ui.cs.advprog.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }
    
    @GetMapping("")
    public ResponseEntity<GeneralResponse> getWallet(@RequestParam("userId") UUID userId) {
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/topup")
    public ResponseEntity<GeneralResponse> topUpWallet(@RequestParam("userId") UUID userId,
                                                       @RequestParam("amount") String amount) {
        walletService.topUpWallet(userId, amount);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Top-up successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/donate")
    public ResponseEntity<GeneralResponse> donate(@RequestParam("userId") UUID userId,
                                                  @RequestParam("amount") String amount) {
        walletService.donateWallet(userId, amount);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Donation successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/withdrawals")
    public ResponseEntity<GeneralResponse> withdrawals(@RequestParam("userId") UUID userId,
                                                       @RequestParam("amount") String amount) {
        walletService.withdrawCampaign(userId, amount);
        Wallet wallet = walletService.getWallet(userId);
        GeneralResponse response = GeneralResponse.from(wallet, "OK", "Campaign withdrawal successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}