package net.javaguides.banking_app.service.implementation;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.exception.BalanceNotEnoughException;
import net.javaguides.banking_app.exception.ResourceNotFoundException;
import net.javaguides.banking_app.mapper.AccountMapper;
import net.javaguides.banking_app.repository.AccountRepository;
import net.javaguides.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account getAccount(Long id) {
        //Account account = accountMapper.toAccount(accountDto);
        return accountRepository.getById(id);
    }

    @Override
    public Account createAccount(AccountDto accountDto) {
        Account account = accountMapper.toAccount(accountDto);
        return accountRepository.save(account);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountDtos = accounts.stream().map(accountMapper::toDto).toList();

        return accountDtos;
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
       Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id" + id));
       account.setBalance(account.getBalance() + amount); // updated the balance
        Account updatedAccount = accountRepository.save(account); // save the updated Account to the db
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account not found with id" + id));
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
            return accountMapper.toDto(account);
        } else {
            throw new BalanceNotEnoughException("Insufficient Balance, please try again");
        }

    }

    @Override
    public String deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account not found with id" + id));

        accountRepository.delete(account);
        return "Successfully deleted the account.";
    }


}
