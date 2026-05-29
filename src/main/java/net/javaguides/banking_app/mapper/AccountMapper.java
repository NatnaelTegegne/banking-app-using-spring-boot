package net.javaguides.banking_app.mapper;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public AccountDto toDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
    }

    public Account  toAccount(AccountDto accountDto) {
        return new Account(
                accountDto.id(),
                accountDto.accountHolderName(),
                accountDto.balance()
        );
    }
}
