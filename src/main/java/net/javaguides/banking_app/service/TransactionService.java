package net.javaguides.banking_app.service;

import net.javaguides.banking_app.dto.TransactionDto;
import java.util.List;

public interface TransactionService {
    List<TransactionDto> getTransactionsByAccountId(Long accountId);
}