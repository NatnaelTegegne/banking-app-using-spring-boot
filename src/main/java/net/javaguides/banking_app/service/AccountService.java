package net.javaguides.banking_app.service;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.dto.TransferFundDto;
import net.javaguides.banking_app.entity.Account;

import java.util.List;

public interface AccountService {

    AccountDto getAccount(Long id);

    Account createAccount(AccountDto accountDto);

    List<AccountDto> getAllAccounts();

    AccountDto deposit(Long id, double amount);

    AccountDto withdraw(Long id, double amount);

    String deleteAccount(Long id);

    void transferFunds(TransferFundDto transferFundDto);
}
