package net.javaguides.banking_app.service.impl;

import net.javaguides.banking_app.dto.TransactionDto;
import net.javaguides.banking_app.entity.Transaction;
import net.javaguides.banking_app.mapper.TransactionMapper;
import net.javaguides.banking_app.repository.TransactionRepository;
import net.javaguides.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId);

        return transactions.stream()
                .map(TransactionMapper::mapToTransactionDto)
                .collect(Collectors.toList());
    }
}