package net.javaguides.banking_app.controller;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.dto.TransferFundDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.mapper.AccountMapper;
import net.javaguides.banking_app.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper){
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PostMapping()
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountdto) {
        Account createdAccount = accountService.createAccount(accountdto);
        return new ResponseEntity<>(accountMapper.toDto(createdAccount), HttpStatus.CREATED);
    }

    @GetMapping("/allAccounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accountDtos = accountService.getAllAccounts();
        return ResponseEntity.ok(accountDtos);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody double amount) {
        AccountDto accountDto = accountService.deposit(id, amount);
        return ResponseEntity.ok(accountDto);

    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id,@RequestBody Map<String, Double> request) {
        AccountDto accountDto = accountService.withdraw(id, request.get("amount"));
        return ResponseEntity.ok(accountDto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        String response = accountService.deleteAccount(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> tranferTo(@RequestBody TransferFundDto transferFundDto) {
        accountService.transferFunds(transferFundDto);

        return new ResponseEntity<>("Transfer Completed.", HttpStatus.ACCEPTED);
    }

}
