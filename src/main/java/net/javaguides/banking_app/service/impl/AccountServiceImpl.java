package net.javaguides.banking_app.service.impl;

import jakarta.transaction.Transactional;
import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;
import net.javaguides.banking_app.entity.Transaction;
import net.javaguides.banking_app.entity.User;
import net.javaguides.banking_app.exception.AccountException;
import net.javaguides.banking_app.mapper.AccountMapper;
import net.javaguides.banking_app.repository.IAccountRepository;
import net.javaguides.banking_app.repository.IUserRepository;
import net.javaguides.banking_app.repository.TransactionRepository;
import net.javaguides.banking_app.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements IAccountService {

    private IAccountRepository accountRepository;
    private IUserRepository userRepository;

    public AccountServiceImpl(IAccountRepository accountRepository, IUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        // 🔥 Giriş yapmış kullanıcıyı al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccountException("User not found"));

        Account account = AccountMapper.mapToAccount(accountDto);
        account.setUser(user); // 🔥 Hesabı kullanıcıya bağla

        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist"));

        // 🔥 Güvenlik kontrolü: Sadece kendi hesabını görebilir
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccountException("Unauthorized access");
        }

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        saveTransaction(id, "DEPOSIT", amount, null, "Deposit to account", total);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        saveTransaction(id, "WITHDRAW", amount, null, "Withdrawal from account", total);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        // 🔥 Sadece giriş yapmış kullanıcının hesaplarını getir
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Account> accounts = accountRepository.findByUserUsername(username);
        return accounts.stream()
                .map(AccountMapper::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getAccountsByUsername(String username) {
        List<Account> accounts = accountRepository.findByUserUsername(username);
        return accounts.stream()
                .map(AccountMapper::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist"));

        // 🔥 Güvenlik kontrolü
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccountException("Unauthorized access");
        }

        accountRepository.deleteById(id);
    }

    @Autowired
    private TransactionRepository transactionRepository;


    private void saveTransaction(Long accountId, String type, double amount,
                                 Long targetAccountId, String description, double balanceAfter) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setTargetAccountId(targetAccountId);
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setBalanceAfter(balanceAfter);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public AccountDto transfer(Long fromAccountId, Long toAccountId, double amount) {

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account does not exist"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Target account does not exist"));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        saveTransaction(fromAccountId, "TRANSFER_OUT", amount, toAccountId,
                "Transfer to account #" + toAccountId, fromAccount.getBalance());

        saveTransaction(toAccountId, "TRANSFER_IN", amount, fromAccountId,
                "Transfer from account #" + fromAccountId, toAccount.getBalance());

        return AccountMapper.mapToAccountDto(fromAccount);
    }
}