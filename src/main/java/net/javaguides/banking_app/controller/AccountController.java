package net.javaguides.banking_app.controller;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.mapper.AccountMapper;
import net.javaguides.banking_app.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Account account= accountService.getAccount(id);
        return ResponseEntity.ok(accountMapper.toDto(account));
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
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id,@RequestBody double amount) {
        AccountDto accountDto = accountService.withdraw(id, amount);
        return ResponseEntity.ok(accountDto);
    }

}
