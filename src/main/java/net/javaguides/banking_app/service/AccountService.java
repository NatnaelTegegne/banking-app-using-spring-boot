package net.javaguides.banking_app.service;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;

import java.util.List;

public interface AccountService {

    public Account getAccount(Long id);

    public Account createAccount(AccountDto accountDto);

    public List<AccountDto> getAllAccounts();

    public AccountDto deposit(Long id, double amount);

    public AccountDto withdraw(Long id, double amount);
}
