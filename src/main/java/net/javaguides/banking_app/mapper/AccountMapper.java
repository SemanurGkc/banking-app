package net.javaguides.banking_app.mapper;

import net.javaguides.banking_app.dto.AccountDto;
import net.javaguides.banking_app.entity.Account;

public class AccountMapper {
    public static Account mapToAccount(AccountDto accountDto){
        Account account = new Account();
        account.setId(accountDto.id());
        account.setAccountHolderName(accountDto.accountHolderName());
        account.setBalance(accountDto.balance());
        return account;
    }
    public static AccountDto mapToAccountDto(Account account){
        AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
        return accountDto;
    }
}
