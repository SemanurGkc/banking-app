package net.javaguides.banking_app.mapper;

import net.javaguides.banking_app.dto.TransactionDto;
import net.javaguides.banking_app.entity.Transaction;

public class TransactionMapper {

    public static TransactionDto mapToTransactionDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getTargetAccountId(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                transaction.getBalanceAfter()
        );
    }
}