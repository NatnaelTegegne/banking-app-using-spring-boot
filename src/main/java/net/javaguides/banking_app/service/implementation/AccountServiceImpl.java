package net.javaguides.banking_app.service.implementation;

import jakarta.transaction.Transactional;
import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.dto.TransactionDto;
import net.javaguides.banking_app.dto.TransferFundDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.entity.Transaction;
import net.javaguides.banking_app.exception.AccountNotFoundException;
import net.javaguides.banking_app.exception.BalanceNotEnoughException;
import net.javaguides.banking_app.mapper.AccountMapper;
import net.javaguides.banking_app.repository.AccountRepository;
import net.javaguides.banking_app.repository.TransactionRepository;
import net.javaguides.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final TransactionRepository transactionRepository;

    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountDto getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found."));
        return accountMapper.toDto(account);
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
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));
       account.setBalance(account.getBalance() + amount); // updated the balance
        Account updatedAccount = accountRepository.save(account); // save the updated Account to the db

        //Log the transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return accountMapper.toDto(updatedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found."));
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            // Log the withdrawal transaction
            Transaction transaction = new Transaction();
            transaction.setAccountId(id);
            transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
            transaction.setAmount(amount);
            transaction.setTimestamp(LocalDateTime.now());

            transactionRepository.save(transaction);

            return accountMapper.toDto(account);
        } else {
            throw new BalanceNotEnoughException("Insufficient Balance, please try again");
        }

    }

    @Override
    public String deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found."));

        accountRepository.delete(account);
        return "Successfully deleted the account.";
    }

    @Override
    @Transactional
    public void transferFunds(TransferFundDto transferFundDto) {
        Account senderAccount = accountRepository.findById(transferFundDto.fromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found."));
        Account receiverAccount = accountRepository.findById(transferFundDto.toAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found."));

        //Debit amount from the sender account
        if (senderAccount.getBalance() >= transferFundDto.amount()) {
            senderAccount.setBalance(senderAccount.getBalance() - transferFundDto.amount());
            accountRepository.save(senderAccount);
            //Credit amount to the receiver account
            receiverAccount.setBalance(receiverAccount.getBalance() + transferFundDto.amount());
            accountRepository.save(receiverAccount);
        } else {
            throw new BalanceNotEnoughException("Not enough funds.");
        }
        // Log the transfer transaction
        // First generate a unique token to link both rows
        String uniqueTransferId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();


        //log senders side of transaction
        Transaction senderTx = new Transaction();
        senderTx.setAmount(-transferFundDto.amount());
        senderTx.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        senderTx.setAccountId(senderAccount.getId());
        senderTx.setTimestamp(now);
        senderTx.setTransferId(uniqueTransferId);

        transactionRepository.save(senderTx);
        // log receivers side of transaction

        Transaction receiverTx = new Transaction();
        receiverTx.setAmount(transferFundDto.amount());
        receiverTx.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        receiverTx.setAccountId(receiverAccount.getId());
        receiverTx.setTimestamp(now);
        receiverTx.setTransferId(uniqueTransferId);

        transactionRepository.save(receiverTx);
    }

    @Override
    public List<TransactionDto> getAccountTransactions(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream().map((transaction) -> toTransactionDto(transaction))
                .collect(Collectors.toList());


    }

    private TransactionDto toTransactionDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp());
    }
}
